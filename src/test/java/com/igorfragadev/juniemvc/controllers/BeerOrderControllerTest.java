package com.igorfragadev.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorfragadev.juniemvc.models.BeerOrderDto;
import com.igorfragadev.juniemvc.models.BeerOrderLineDto;
import com.igorfragadev.juniemvc.services.BeerOrderService;
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

import java.math.BigDecimal;
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

@WebMvcTest(BeerOrderController.class)
@Import(BeerOrderControllerTest.BeerOrderServiceConfig.class)
class BeerOrderControllerTest {

    @TestConfiguration
    static class BeerOrderServiceConfig {
        @Bean
        BeerOrderService beerOrderService() {
            return Mockito.mock(BeerOrderService.class);
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BeerOrderService beerOrderService;

    BeerOrderDto testBeerOrderDto;
    BeerOrderLineDto testBeerOrderLineDto;

    @BeforeEach
    void setUp() {
        testBeerOrderLineDto = BeerOrderLineDto.builder()
                .id(1)
                .beerId(1)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        testBeerOrderDto = BeerOrderDto.builder()
                .id(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .beerOrderLines(Arrays.asList(testBeerOrderLineDto))
                .build();
    }

    @Test
    void getAllBeerOrders() throws Exception {
        given(beerOrderService.getAllBeerOrders()).willReturn(Arrays.asList(testBeerOrderDto));

        mockMvc.perform(get("/api/v1/beer-orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].customerRef", is("TEST-REF-001")));
    }

    @Test
    void getBeerOrderById() throws Exception {
        given(beerOrderService.getBeerOrderById(1)).willReturn(Optional.of(testBeerOrderDto));

        mockMvc.perform(get("/api/v1/beer-orders/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerRef", is("TEST-REF-001")));
    }

    @Test
    void getBeerOrderByIdNotFound() throws Exception {
        given(beerOrderService.getBeerOrderById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer-orders/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeerOrder() throws Exception {
        BeerOrderDto beerOrderDtoToSave = BeerOrderDto.builder()
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .beerOrderLines(Arrays.asList(testBeerOrderLineDto))
                .build();

        BeerOrderDto savedBeerOrderDto = BeerOrderDto.builder()
                .id(2)
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .beerOrderLines(Arrays.asList(testBeerOrderLineDto))
                .build();

        given(beerOrderService.saveBeerOrder(any(BeerOrderDto.class))).willReturn(savedBeerOrderDto);

        mockMvc.perform(post("/api/v1/beer-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderDtoToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.customerRef", is("NEW-REF-001")));
    }

    @Test
    void updateBeerOrder() throws Exception {
        BeerOrderDto beerOrderDtoToUpdate = BeerOrderDto.builder()
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        BeerOrderDto updatedBeerOrderDto = BeerOrderDto.builder()
                .id(1)
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        given(beerOrderService.updateBeerOrder(anyInt(), any(BeerOrderDto.class))).willReturn(Optional.of(updatedBeerOrderDto));

        mockMvc.perform(put("/api/v1/beer-orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerRef", is("UPDATED-REF-001")))
                .andExpect(jsonPath("$.status", is("UPDATED")));
    }

    @Test
    void updateBeerOrderNotFound() throws Exception {
        BeerOrderDto beerOrderDtoToUpdate = BeerOrderDto.builder()
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        given(beerOrderService.updateBeerOrder(anyInt(), any(BeerOrderDto.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beer-orders/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderDtoToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBeerOrder() throws Exception {
        given(beerOrderService.deleteBeerOrder(1)).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer-orders/1"))
                .andExpect(status().isNoContent());

        verify(beerOrderService).deleteBeerOrder(1);
    }

    @Test
    void deleteBeerOrderNotFound() throws Exception {
        given(beerOrderService.deleteBeerOrder(999)).willReturn(false);

        mockMvc.perform(delete("/api/v1/beer-orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeerOrderValidationError() throws Exception {
        BeerOrderDto invalidBeerOrderDto = BeerOrderDto.builder()
                // Missing required fields
                .customerRef("")
                .paymentAmount(new BigDecimal("-1.00"))
                .build();

        mockMvc.perform(post("/api/v1/beer-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBeerOrderDto)))
                .andExpect(status().isBadRequest());
    }
}