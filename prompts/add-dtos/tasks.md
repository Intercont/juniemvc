# DTO Integration Task List

## 1. Create BeerDto Class
- [x] 1.1. Create a new package `com.igorfragadev.juniemvc.models`
- [x] 1.2. Create a new class `BeerDto` with the following:
  - [x] 1.2.1. Properties matching the Beer entity (id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, updatedDate)
  - [x] 1.2.2. Add Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)
  - [x] 1.2.3. Add Jakarta Validation annotations:
    - [x] 1.2.3.1. @NotBlank for String fields (beerName, beerStyle, upc)
    - [x] 1.2.3.2. @NotNull for required fields
    - [x] 1.2.3.3. @Positive for numeric fields (quantityOnHand, price)

## 2. Create BeerMapper Interface
- [x] 2.2. Create a new package `com.igorfragadev.juniemvc.mappers`
- [x] 2.3. Create a new interface `BeerMapper` with the following:
  - [x] 2.3.1. @Mapper annotation with componentModel = "spring"
  - [x] 2.3.2. Method for mapping Beer entity to BeerDto
  - [x] 2.3.3. Method for mapping BeerDto to Beer entity
  - [x] 2.3.4. Configuration to ignore id, createdDate, and updatedDate when mapping from DTO to entity

## 3. Update BeerService Interface
- [x] 3.1. Modify the BeerService interface to use BeerDto instead of Beer entity:
  - [x] 3.1.1. Update getAllBeers() to return List<BeerDto>
  - [x] 3.1.2. Update getBeerById() to return Optional<BeerDto>
  - [x] 3.1.3. Update saveBeer() to accept and return BeerDto
  - [x] 3.1.4. Update updateBeer() to accept BeerDto and return Optional<BeerDto>
  - [x] 3.1.5. Update deleteBeer() method signature if needed

## 4. Update BeerServiceImpl
- [x] 4.1. Inject BeerMapper into BeerServiceImpl
- [x] 4.2. Update getAllBeers() implementation to map entities to DTOs
- [x] 4.3. Update getBeerById() implementation to map entity to DTO
- [x] 4.4. Update saveBeer() implementation to map DTO to entity and back
- [x] 4.5. Update updateBeer() implementation to map DTO to entity and back
- [x] 4.6. Update deleteBeer() implementation if needed

## 5. Update BeerController
- [x] 5.1. Update import statements to use BeerDto
- [x] 5.2. Add @Valid annotation to request body parameters
- [x] 5.3. Update getAllBeers() endpoint to work with BeerDto
- [x] 5.4. Update getBeerById() endpoint to work with BeerDto
- [x] 5.5. Update createBeer() endpoint to work with BeerDto
- [x] 5.6. Update updateBeer() endpoint to work with BeerDto
- [x] 5.7. Update deleteBeer() endpoint if needed

## 6. Add Global Exception Handler
- [x] 6.1. Create a new package `com.igorfragadev.juniemvc.exceptions`
- [x] 6.2. Create a new class `GlobalExceptionHandler` with:
  - [x] 6.2.1. @ControllerAdvice annotation
  - [x] 6.2.2. @ExceptionHandler for MethodArgumentNotValidException
  - [x] 6.2.3. Logic to extract and return validation errors

## 7. Update Tests
- [x] 7.1. Create unit tests for BeerMapper
  - [x] 7.1.1. Test mapping from Beer to BeerDto
  - [x] 7.1.2. Test mapping from BeerDto to Beer
- [x] 7.2. Update BeerServiceImplTest to work with DTOs
  - [x] 7.2.1. Update test data to use BeerDto
  - [x] 7.2.2. Update assertions to check DTO properties
- [x] 7.3. Update BeerControllerTest to work with DTOs
  - [x] 7.3.1. Update test data to use BeerDto
  - [x] 7.3.2. Update assertions to check DTO properties
- [x] 7.4. Add validation tests
  - [x] 7.4.1. Test validation for required fields
  - [x] 7.4.2. Test validation for positive numeric values

## 8. Final Testing and Verification
- [x] 8.1. Run all tests to ensure everything works correctly
- [x] 8.2. Manually test the API endpoints
- [x] 8.3. Verify validation errors are handled correctly
- [x] 8.4. Check for any performance issues
- [x] 8.5. Review code for any potential improvements
