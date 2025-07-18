# BeerOrderShipment Implementation Tasks

## Database Migration
1. [x] Create a new Flyway migration script `V3__create_beer_order_shipment_table.sql` in `src/main/resources/db/migration`
2. [x] Add table definition with columns: id, beer_order_id, shipment_date, carrier, tracking_number, created_at, updated_at
3. [x] Add foreign key constraint to beer_order table

## Entity Creation
4. [x] Create `BeerOrderShipment` entity class with required fields and annotations
5. [x] Implement ManyToOne relationship with BeerOrder
6. [x] Update `BeerOrder` entity to include OneToMany relationship with BeerOrderShipment
7. [x] Add helper method in BeerOrder to maintain bidirectional relationship

## Data Transfer Objects
8. [x] Create `BeerOrderShipmentDto` class with all necessary fields
9. [x] Add appropriate annotations and builder pattern

## Mapper
10. [x] Create `BeerOrderShipmentMapper` interface
11. [x] Implement methods to convert between entity and DTO
12. [x] Configure appropriate mappings with @Mapping annotations

## Repository
13. [x] Create `BeerOrderShipmentRepository` interface extending JpaRepository
14. [x] Add method to find shipments by beer order ID

## Service Layer
15. [x] Create `BeerOrderShipmentService` interface with required methods
16. [x] Implement `BeerOrderShipmentServiceImpl` class
17. [x] Add proper transaction annotations (@Transactional)
18. [x] Implement all service methods (getAllBeerOrderShipments, getShipmentsByBeerOrderId, etc.)

## Controller
19. [x] Create `BeerOrderShipmentController` class
20. [x] Implement REST endpoints for CRUD operations
21. [x] Add proper request mappings and response entities
22. [x] Implement validation for request bodies

## Testing
23. [x] Create entity tests for BeerOrderShipment
24. [x] Create repository tests for BeerOrderShipmentRepository
25. [x] Create mapper tests for BeerOrderShipmentMapper
26. [x] Create service tests for BeerOrderShipmentService
27. [x] Create controller tests for BeerOrderShipmentController

## Documentation
28. [x] Update OpenAPI documentation to include new endpoints
29. [x] Add schema definitions for BeerOrderShipment

## Validation
30. [x] Run all tests to ensure everything works correctly
31. [x] Validate OpenAPI documentation with linting tool
32. [x] Fix any issues found during testing and validation
