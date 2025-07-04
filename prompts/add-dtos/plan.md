# Implementation Plan for DTO Integration in Beer API

## Overview
This document outlines the detailed implementation plan for integrating Data Transfer Objects (DTOs) into the Beer API. The current implementation directly exposes JPA entities in the REST API, which tightly couples the database schema with the API contract. This plan describes the steps needed to implement DTOs to provide a clear separation between the API layer and the persistence layer.

## Current State Analysis
The current implementation has the following components:
- **Beer Entity**: A JPA entity with properties like id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, and updatedDate.
- **BeerRepository**: Extends JpaRepository for CRUD operations on the Beer entity.
- **BeerService Interface**: Defines operations like getAllBeers(), getBeerById(), saveBeer(), updateBeer(), and deleteBeer(), all working directly with the Beer entity.
- **BeerServiceImpl**: Implements the BeerService interface using the BeerRepository.
- **BeerController**: Exposes REST endpoints for CRUD operations, directly using the Beer entity in request and response bodies.

## Implementation Steps

### 1. Create BeerDto Class
1. Create a new package `com.igorfragadev.juniemvc.models`
2. Create a new class `BeerDto` with the following:
   - Properties matching the Beer entity (id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, updatedDate)
   - Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)
   - Jakarta Validation annotations:
     - @NotBlank for String fields (beerName, beerStyle, upc)
     - @NotNull for required fields
     - @Positive for numeric fields (quantityOnHand, price)

```java
package com.igorfragadev.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
    private Integer id;
    private Integer version;
    
    @NotBlank(message = "Beer name is required")
    private String beerName;
    
    @NotBlank(message = "Beer style is required")
    private String beerStyle;
    
    @NotBlank(message = "UPC is required")
    private String upc;
    
    @NotNull(message = "Quantity on hand is required")
    @Positive(message = "Quantity on hand must be positive")
    private Integer quantityOnHand;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
```

### 2. Create BeerMapper Interface
1. Create a new package `com.igorfragadev.juniemvc.mappers`
2. Create a new interface `BeerMapper` with the following:
   - @Mapper annotation with componentModel = "spring"
   - Methods for mapping between Beer entity and BeerDto
   - Configuration to ignore id, createdDate, and updatedDate when mapping from DTO to entity

```java
package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.models.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}
```

### 3. Update BeerService Interface
1. Modify the BeerService interface to use BeerDto instead of Beer entity:

```java
package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.models.BeerDto;

import java.util.List;
import java.util.Optional;

public interface BeerService {
    /**
     * Get all beers
     * @return List of all beers
     */
    List<BeerDto> getAllBeers();

    /**
     * Get a beer by its ID
     * @param id The beer ID
     * @return Optional containing the beer if found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Save a new beer
     * @param beerDto The beer to save
     * @return The saved beer
     */
    BeerDto saveBeer(BeerDto beerDto);

    /**
     * Update an existing beer
     * @param id The beer ID
     * @param beerDto The updated beer data
     * @return Optional containing the updated beer if found
     */
    Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto);

    /**
     * Delete a beer by its ID
     * @param id The beer ID
     * @return true if the beer was deleted, false if not found
     */
    boolean deleteBeer(Integer id);
}
```

### 4. Update BeerServiceImpl
1. Modify the BeerServiceImpl to use BeerMapper for converting between entities and DTOs:

```java
package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.mappers.BeerMapper;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    public List<BeerDto> getAllBeers() {
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    public Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    Beer beer = beerMapper.beerDtoToBeer(beerDto);
                    existingBeer.setBeerName(beer.getBeerName());
                    existingBeer.setBeerStyle(beer.getBeerStyle());
                    existingBeer.setUpc(beer.getUpc());
                    existingBeer.setPrice(beer.getPrice());
                    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    return beerRepository.save(existingBeer);
                })
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public boolean deleteBeer(Integer id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

### 5. Update BeerController
1. Modify the BeerController to use BeerDto and add validation:

```java
package com.igorfragadev.juniemvc.controllers;

import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.services.BeerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping
    public ResponseEntity<List<BeerDto>> getAllBeers() {
        return new ResponseEntity<>(beerService.getAllBeers(), HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .map(beer -> new ResponseEntity<>(beer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerDto beerDto) {
        BeerDto savedBeer = beerService.saveBeer(beerDto);
        return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") Integer beerId, @Valid @RequestBody BeerDto beerDto) {
        return beerService.updateBeer(beerId, beerDto)
                .map(updatedBeer -> new ResponseEntity<>(updatedBeer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
        if (beerService.deleteBeer(beerId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
```

### 6. Add Global Exception Handler
1. Create a new package `com.igorfragadev.juniemvc.exceptions`
2. Create a new class `GlobalExceptionHandler` to handle validation errors:

```java
package com.igorfragadev.juniemvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
```

### 7. Update Tests
1. Update all tests to work with BeerDto instead of Beer entity.
2. Add tests for validation rules.

## Testing Strategy
1. Unit tests for BeerMapper to ensure correct mapping between entities and DTOs.
2. Unit tests for BeerService to ensure correct business logic with DTOs.
3. Integration tests for BeerController to ensure correct API behavior with DTOs.
4. Validation tests to ensure input validation works correctly.

## Benefits of Implementation
1. **Decoupling**: The API contract is decoupled from the database schema, allowing each to evolve independently.
2. **Security**: Sensitive fields can be excluded from the API response if needed.
3. **Validation**: Input validation is centralized at the API boundary.
4. **Flexibility**: Different DTOs can be created for different use cases in the future.
5. **Performance**: Only necessary data is transferred over the network.

## Timeline
1. Create BeerDto class and add validation annotations - 1 hour
2. Add MapStruct dependency and create BeerMapper - 1 hour
3. Update BeerService interface and implementation - 2 hours
4. Update BeerController - 1 hour
5. Add GlobalExceptionHandler - 1 hour
6. Update tests - 2 hours
7. Testing and bug fixing - 2 hours

Total estimated time: 10 hours