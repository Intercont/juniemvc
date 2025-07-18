package com.igorfragadev.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;
import com.igorfragadev.juniemvc.services.BeerOrderShipmentService;
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

import java.time.LocalDate;
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

@WebMvcTest(BeerOrderShipmentController.class)
@Import(BeerOrderShipmentControllerTest.BeerOrderShipmentServiceConfig.class)
class BeerOrderShipmentControllerTest {

    @TestConfiguration
    static class BeerOrderShipmentServiceConfig {
        @Bean
        BeerOrderShipmentService beerOrderShipmentService() {
            return Mockito.mock(BeerOrderShipmentService.class);
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BeerOrderShipmentService beerOrderShipmentService;

    BeerOrderShipmentDto testBeerOrderShipmentDto;

    @BeforeEach
    void setUp() {
        testBeerOrderShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .beerOrderId(1)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();
    }

    @Test
    void getAllBeerOrderShipments() throws Exception {
        given(beerOrderShipmentService.getAllBeerOrderShipments()).willReturn(Arrays.asList(testBeerOrderShipmentDto));

        mockMvc.perform(get("/api/v1/beer-order-shipments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerOrderId", is(1)))
                .andExpect(jsonPath("$[0].carrier", is("UPS")));
    }

    @Test
    void getShipmentsByBeerOrderId() throws Exception {
        given(beerOrderShipmentService.getShipmentsByBeerOrderId(1)).willReturn(Arrays.asList(testBeerOrderShipmentDto));

        mockMvc.perform(get("/api/v1/beer-order-shipments/beer-order/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerOrderId", is(1)));
    }

    @Test
    void getBeerOrderShipmentById() throws Exception {
        given(beerOrderShipmentService.getBeerOrderShipmentById(1)).willReturn(Optional.of(testBeerOrderShipmentDto));

        mockMvc.perform(get("/api/v1/beer-order-shipments/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerOrderId", is(1)))
                .andExpect(jsonPath("$.carrier", is("UPS")));
    }

    @Test
    void getBeerOrderShipmentByIdNotFound() throws Exception {
        given(beerOrderShipmentService.getBeerOrderShipmentById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer-order-shipments/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeerOrderShipment() throws Exception {
        BeerOrderShipmentDto shipmentDtoToSave = BeerOrderShipmentDto.builder()
                .beerOrderId(1)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        BeerOrderShipmentDto savedShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .beerOrderId(1)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        given(beerOrderShipmentService.saveBeerOrderShipment(any(BeerOrderShipmentDto.class))).willReturn(savedShipmentDto);

        mockMvc.perform(post("/api/v1/beer-order-shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentDtoToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerOrderId", is(1)))
                .andExpect(jsonPath("$.carrier", is("UPS")));
    }

    @Test
    void updateBeerOrderShipment() throws Exception {
        BeerOrderShipmentDto shipmentDtoToUpdate = BeerOrderShipmentDto.builder()
                .beerOrderId(1)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        BeerOrderShipmentDto updatedShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .beerOrderId(1)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        given(beerOrderShipmentService.updateBeerOrderShipment(anyInt(), any(BeerOrderShipmentDto.class))).willReturn(Optional.of(updatedShipmentDto));

        mockMvc.perform(put("/api/v1/beer-order-shipments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.carrier", is("FedEx")))
                .andExpect(jsonPath("$.trackingNumber", is("9876543210")));
    }

    @Test
    void updateBeerOrderShipmentNotFound() throws Exception {
        BeerOrderShipmentDto shipmentDtoToUpdate = BeerOrderShipmentDto.builder()
                .beerOrderId(1)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        given(beerOrderShipmentService.updateBeerOrderShipment(anyInt(), any(BeerOrderShipmentDto.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beer-order-shipments/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentDtoToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBeerOrderShipment() throws Exception {
        given(beerOrderShipmentService.deleteBeerOrderShipment(1)).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer-order-shipments/1"))
                .andExpect(status().isNoContent());

        verify(beerOrderShipmentService).deleteBeerOrderShipment(1);
    }

    @Test
    void deleteBeerOrderShipmentNotFound() throws Exception {
        given(beerOrderShipmentService.deleteBeerOrderShipment(999)).willReturn(false);

        mockMvc.perform(delete("/api/v1/beer-order-shipments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeerOrderShipmentValidationError() throws Exception {
        BeerOrderShipmentDto invalidShipmentDto = BeerOrderShipmentDto.builder()
                // Missing required beerOrderId
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        mockMvc.perform(post("/api/v1/beer-order-shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidShipmentDto)))
                .andExpect(status().isBadRequest());
    }
}