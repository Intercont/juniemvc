# Data Transfer Object (DTO) Implementation Requirements

## Overview
This document outlines the requirements for implementing Data Transfer Objects (DTOs) in the Beer API. The current implementation directly exposes JPA entities in the REST API, which tightly couples the database schema with the API contract. Implementing DTOs will provide a clear separation between the API layer and the persistence layer.

## Objectives
- Decouple the API contract from the database schema
- Provide a clear boundary between layers of the application
- Enable independent evolution of the API and database schema
- Implement proper validation at the API boundary
- Follow best practices for Spring Boot applications

## Requirements

### 1. Create BeerDto Class
- Create a new POJO class called `BeerDto` in a new package `com.igorfragadev.juniemvc.models`
- The DTO should include the following properties from the Beer entity:
  - Integer id
  - Integer version
  - String beerName
  - String beerStyle
  - String upc
  - Integer quantityOnHand
  - BigDecimal price
  - LocalDateTime createdDate
  - LocalDateTime updatedDate
- Use Lombok annotations to reduce boilerplate code:
  - `@Getter`
  - `@Setter`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`
  - `@Builder`
- Add appropriate validation annotations from Jakarta Validation:
  - `@NotBlank` for String fields (beerName, beerStyle, upc)
  - `@NotNull` for required fields
  - `@Positive` for numeric fields that should be positive (quantityOnHand, price)

### 2. Create MapStruct Mapper
- Create a new interface called `BeerMapper` in a new package `com.igorfragadev.juniemvc.mappers`
- Use the `@Mapper` annotation with `componentModel = "spring"` to make it a Spring bean
- Define the following mapping methods:
  - `BeerDto beerToBeerDto(Beer beer)` - Convert from entity to DTO
  - `Beer beerDtoToBeer(BeerDto beerDto)` - Convert from DTO to entity
- When mapping from DTO to entity, ignore the following properties:
  - id
  - createdDate
  - updatedDate
- These properties should be managed by the database/JPA, not set from client input

### 3. Update Service Layer
- Modify the `BeerService` interface to use `BeerDto` instead of `Beer` entity:
  - `List<BeerDto> getAllBeers()`
  - `Optional<BeerDto> getBeerById(Integer id)`
  - `BeerDto saveBeer(BeerDto beerDto)`
  - `Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto)`
  - `boolean deleteBeer(Integer id)`
- Update the `BeerServiceImpl` class to:
  - Inject the `BeerMapper` using constructor injection
  - Use the mapper to convert between DTOs and entities
  - Maintain the same business logic but with the DTO/entity conversions

### 4. Update Controller Layer
- Modify the `BeerController` to use `BeerDto` instead of `Beer` entity:
  - Update all method signatures to use `BeerDto`
  - Add `@Valid` annotation to validate request bodies
  - Return appropriate HTTP status codes and error messages for validation failures
- The REST API endpoints should remain the same:
  - GET `/api/v1/beers` - Get all beers
  - GET `/api/v1/beers/{beerId}` - Get beer by ID
  - POST `/api/v1/beers` - Create a new beer
  - PUT `/api/v1/beers/{beerId}` - Update an existing beer
  - DELETE `/api/v1/beers/{beerId}` - Delete a beer

## Implementation Guidelines
1. **Separation of Concerns**: Ensure clear separation between API, service, and persistence layers
2. **Validation**: Implement proper validation at the API boundary using Jakarta Validation
3. **Error Handling**: Return appropriate HTTP status codes and error messages for validation failures
4. **Testing**: Update existing tests to work with the new DTO-based implementation
5. **Documentation**: Update API documentation to reflect the new DTO-based contract

## Benefits
- **Decoupling**: The API contract is decoupled from the database schema, allowing each to evolve independently
- **Security**: Sensitive fields can be excluded from the API response
- **Validation**: Input validation is centralized at the API boundary
- **Flexibility**: Different DTOs can be created for different use cases (e.g., create vs. update)
- **Performance**: Only necessary data is transferred over the network