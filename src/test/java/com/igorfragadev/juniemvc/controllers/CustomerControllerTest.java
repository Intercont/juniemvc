package com.igorfragadev.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorfragadev.juniemvc.models.CustomerDto;
import com.igorfragadev.juniemvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(CustomerControllerTest.CustomerServiceConfig.class)
class CustomerControllerTest {

    @TestConfiguration
    static class CustomerServiceConfig {
        @Bean
        CustomerService customerService() {
            return Mockito.mock(CustomerService.class);
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerService customerService;

    CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();
    }

    @Test
    void getAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(Arrays.asList(testCustomerDto));

        mockMvc.perform(get("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    void getCustomerById() throws Exception {
        given(customerService.getCustomerById(1)).willReturn(Optional.of(testCustomerDto));

        mockMvc.perform(get("/api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer() throws Exception {
        CustomerDto customerDtoToSave = CustomerDto.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();

        CustomerDto savedCustomerDto = CustomerDto.builder()
                .id(2)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();

        given(customerService.saveCustomer(any(CustomerDto.class))).willReturn(savedCustomerDto);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDtoToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Jane Smith")));
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDto customerDtoToUpdate = CustomerDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .id(1)
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        given(customerService.updateCustomer(anyInt(), any(CustomerDto.class))).willReturn(Optional.of(updatedCustomerDto));

        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Updated")))
                .andExpect(jsonPath("$.email", is("john.updated@example.com")));
    }

    @Test
    void updateCustomerNotFound() throws Exception {
        CustomerDto customerDtoToUpdate = CustomerDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        given(customerService.updateCustomer(anyInt(), any(CustomerDto.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/customers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDtoToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomer() throws Exception {
        given(customerService.deleteCustomer(1)).willReturn(true);

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1);
    }

    @Test
    void deleteCustomerNotFound() throws Exception {
        given(customerService.deleteCustomer(999)).willReturn(false);

        mockMvc.perform(delete("/api/v1/customers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomerValidationError() throws Exception {
        CustomerDto invalidCustomerDto = CustomerDto.builder()
                // Missing required fields
                .name("")
                .email("invalid-email")
                .addressLine1("")
                .city("")
                .state("")
                .postalCode("")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomerDto)))
                .andExpect(status().isBadRequest());
    }
}