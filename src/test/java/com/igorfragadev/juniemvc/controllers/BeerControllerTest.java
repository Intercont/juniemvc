package com.igorfragadev.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.models.BeerPathDto;
import com.igorfragadev.juniemvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@Import(BeerControllerTest.BeerServiceConfig.class)
class BeerControllerTest {

    @TestConfiguration
    static class BeerServiceConfig {
        @Bean
        BeerService beerService() {
            return Mockito.mock(BeerService.class);
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BeerService beerService;

    BeerDto testBeerDto;

    @BeforeEach
    void setUp() {
        testBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void getAllBeers() throws Exception {
        given(beerService.getAllBeers()).willReturn(Arrays.asList(testBeerDto));

        mockMvc.perform(get("/api/v1/beers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerName", is("Test Beer")));
    }

    @Test
    void getBeerById() throws Exception {
        given(beerService.getBeerById(1)).willReturn(Optional.of(testBeerDto));

        mockMvc.perform(get("/api/v1/beers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeer() throws Exception {
        BeerDto beerDtoToSave = BeerDto.builder()
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();

        BeerDto savedBeerDto = BeerDto.builder()
                .id(2)
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();

        given(beerService.saveBeer(any(BeerDto.class))).willReturn(savedBeerDto);

        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDtoToSave)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.beerName", is("New Beer")));
    }

    @Test
    void updateBeer() throws Exception {
        BeerDto beerDtoToUpdate = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        BeerDto updatedBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        given(beerService.updateBeer(anyInt(), any(BeerDto.class))).willReturn(Optional.of(updatedBeerDto));

        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Updated Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Stout")));
    }

    @Test
    void updateBeerNotFound() throws Exception {
        BeerDto beerDtoToUpdate = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        given(beerService.updateBeer(anyInt(), any(BeerDto.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDtoToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBeer() throws Exception {
        given(beerService.deleteBeer(1)).willReturn(true);

        mockMvc.perform(delete("/api/v1/beers/1"))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeer(1);
    }

    @Test
    void deleteBeerNotFound() throws Exception {
        given(beerService.deleteBeer(999)).willReturn(false);

        mockMvc.perform(delete("/api/v1/beers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeerValidationError() throws Exception {
        BeerDto invalidBeerDto = BeerDto.builder()
                // Missing required fields
                .beerName("")
                .beerStyle("")
                .upc("")
                .price(new BigDecimal("-1.00"))
                .quantityOnHand(-1)
                .build();

        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBeerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBeersWithPagination() throws Exception {
        // Create a list of beers
        List<BeerDto> beers = Arrays.asList(
            testBeerDto,
            BeerDto.builder()
                .id(2)
                .beerName("Another Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build()
        );

        // Create a Page of beers
        Page<BeerDto> beerPage = new PageImpl<>(beers, PageRequest.of(0, 10), beers.size());

        // Mock the service method
        given(beerService.getAllBeers(any(), any(), any(Pageable.class))).willReturn(beerPage);

        // Perform the request and verify the response
        mockMvc.perform(get("/api/v1/beers")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].beerName", is("Another Beer")));
    }

    @Test
    void getAllBeersWithPaginationAndBeerName() throws Exception {
        // Create a list of beers filtered by name
        List<BeerDto> beers = Arrays.asList(testBeerDto);

        // Create a Page of beers
        Page<BeerDto> beerPage = new PageImpl<>(beers, PageRequest.of(0, 10), beers.size());

        // Mock the service method with beerName parameter
        given(beerService.getAllBeers(Mockito.eq("Test"), Mockito.isNull(), any(Pageable.class))).willReturn(beerPage);

        // Perform the request with beerName parameter and verify the response
        mockMvc.perform(get("/api/v1/beers")
                .param("page", "0")
                .param("size", "10")
                .param("beerName", "Test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerName", is("Test Beer")));
    }

    @Test
    void getAllBeersWithPaginationAndBeerStyle() throws Exception {
        // Create a list of beers filtered by style
        List<BeerDto> beers = Arrays.asList(testBeerDto);

        // Create a Page of beers
        Page<BeerDto> beerPage = new PageImpl<>(beers, PageRequest.of(0, 10), beers.size());

        // Mock the service method with beerStyle parameter
        given(beerService.getAllBeers(Mockito.isNull(), Mockito.eq("IPA"), any(Pageable.class))).willReturn(beerPage);

        // Perform the request with beerStyle parameter and verify the response
        mockMvc.perform(get("/api/v1/beers")
                .param("page", "0")
                .param("size", "10")
                .param("beerStyle", "IPA")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerStyle", is("IPA")));
    }

    @Test
    void getAllBeersWithPaginationAndBeerNameAndBeerStyle() throws Exception {
        // Create a list of beers filtered by name and style
        List<BeerDto> beers = Arrays.asList(testBeerDto);

        // Create a Page of beers
        Page<BeerDto> beerPage = new PageImpl<>(beers, PageRequest.of(0, 10), beers.size());

        // Mock the service method with both beerName and beerStyle parameters
        given(beerService.getAllBeers(Mockito.eq("Test"), Mockito.eq("IPA"), any(Pageable.class))).willReturn(beerPage);

        // Perform the request with both parameters and verify the response
        mockMvc.perform(get("/api/v1/beers")
                .param("page", "0")
                .param("size", "10")
                .param("beerName", "Test")
                .param("beerStyle", "IPA")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$.content[0].beerStyle", is("IPA")));
    }

    @Test
    void patchBeer() throws Exception {
        // Create a BeerPathDto with only the fields to update
        BeerPathDto beerPathDto = BeerPathDto.builder()
                .beerName("Patched Beer")
                .price(new BigDecimal("16.99"))
                .build();

        // Create the expected updated BeerDto
        BeerDto updatedBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Patched Beer")
                .beerStyle("IPA") // Unchanged
                .upc("123456") // Unchanged
                .price(new BigDecimal("16.99"))
                .quantityOnHand(100) // Unchanged
                .build();

        // Mock the service method
        given(beerService.patchBeer(anyInt(), any(BeerPathDto.class))).willReturn(Optional.of(updatedBeerDto));

        // Perform the request and verify the response
        mockMvc.perform(patch("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerPathDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Patched Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")))
                .andExpect(jsonPath("$.price", is(16.99)));
    }

    @Test
    void patchBeerNotFound() throws Exception {
        // Create a BeerPathDto with only the fields to update
        BeerPathDto beerPathDto = BeerPathDto.builder()
                .beerName("Patched Beer")
                .price(new BigDecimal("16.99"))
                .build();

        // Mock the service method to return empty (beer not found)
        given(beerService.patchBeer(anyInt(), any(BeerPathDto.class))).willReturn(Optional.empty());

        // Perform the request and verify the response
        mockMvc.perform(patch("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerPathDto)))
                .andExpect(status().isNotFound());
    }
}
