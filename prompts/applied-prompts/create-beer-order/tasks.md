# Beer Order System Implementation Tasks

## Entity Layer Implementation
1. [x] Update Beer Entity
   - [x] Add the one-to-many relationship with BeerOrderLine
   - [x] Add helper methods to maintain bidirectional relationship if needed

2. [x] Create BeerOrder Entity
   - [x] Implement as specified in the requirements
   - [x] Include the one-to-many relationship with BeerOrderLine
   - [x] Add helper methods to maintain bidirectional relationship

3. [x] Create BeerOrderLine Entity
   - [x] Implement as specified in the requirements
   - [x] Include many-to-one relationships with Beer and BeerOrder

## Repository Layer Implementation
4. [x] Create BeerOrderRepository
   - [x] Extend JpaRepository for basic CRUD operations
   - [x] Add any custom query methods if needed

5. [x] Create BeerOrderLineRepository
   - [x] Extend JpaRepository for basic CRUD operations
   - [x] Add any custom query methods if needed

## DTO Layer Implementation
6. [x] Create BeerOrderDto
   - [x] Implement as specified in the requirements
   - [x] Include validation annotations for required fields
   - [x] Include a list of BeerOrderLineDto objects

7. [x] Create BeerOrderLineDto
   - [x] Implement as specified in the requirements
   - [x] Include validation annotations for required fields
   - [x] Include a reference to BeerDto for display purposes

## Mapper Layer Implementation
8. [x] Create BeerOrderMapper
   - [x] Use MapStruct with Spring component model
   - [x] Configure to use BeerOrderLineMapper for nested mappings
   - [x] Ignore fields that should not be mapped (e.g., IDs for new entities)

9. [x] Create BeerOrderLineMapper
   - [x] Use MapStruct with Spring component model
   - [x] Configure to use BeerMapper for nested mappings
   - [x] Implement mapping between beerId and Beer entity
   - [x] Ignore fields that should not be mapped

## Service Layer Implementation
10. [x] Create BeerOrderService Interface
    - [x] Define methods for CRUD operations as specified in the requirements
    - [x] Include clear method documentation

11. [x] Create BeerOrderServiceImpl
    - [x] Implement the BeerOrderService interface
    - [x] Use constructor injection for dependencies
    - [x] Apply @Transactional annotations with appropriate settings
    - [x] Implement business logic for creating and updating orders
    - [x] Handle relationships between entities properly

## Controller Layer Implementation
12. [x] Create BeerOrderController
    - [x] Implement RESTful endpoints for CRUD operations
    - [x] Use proper request mappings and HTTP methods
    - [x] Return appropriate HTTP status codes
    - [x] Validate request bodies
    - [x] Handle exceptions properly

## Testing
13. [x] Unit Tests
    - [x] Write tests for mappers
    - [x] Write tests for service implementations
    - [x] Use mocks for dependencies

14. [x] Integration Tests
    - [x] Write tests for repositories
    - [x] Write tests for controllers
    - [x] Use test data builders for consistent test data

## Improvements to Existing Code
15. [x] Add @Transactional Annotations
    - [x] Add @Transactional(readOnly = true) to query-only methods in BeerServiceImpl
    - [x] Add @Transactional to data-modifying methods in BeerServiceImpl

16. [x] Enhance Exception Handling
    - [x] Update GlobalExceptionHandler if needed for new exceptions

17. [x] Run all tests to ensure everything works correctly
    - [x] Fix if anything is not working as expected
