# Customer Entity Implementation Tasks

## 1. Create Customer Entity
- [x] 1.1. Create a new Java class `Customer` in the `com.igorfragadev.juniemvc.entities` package
- [x] 1.2. Implement all required properties:
  - [x] 1.2.1. `id`: Integer, primary key, auto-generated
  - [x] 1.2.2. `version`: Integer, for optimistic locking
  - [x] 1.2.3. `name`: String, not null
  - [x] 1.2.4. `email`: String
  - [x] 1.2.5. `phone`: String
  - [x] 1.2.6. `addressLine1`: String, not null
  - [x] 1.2.7. `addressLine2`: String, optional
  - [x] 1.2.8. `city`: String, not null
  - [x] 1.2.9. `state`: String, not null
  - [x] 1.2.10. `postalCode`: String, not null
  - [x] 1.2.11. `createdAt`: LocalDateTime, auto-generated on creation
  - [x] 1.2.12. `updatedAt`: LocalDateTime, auto-updated on modification
- [x] 1.3. Add OneToMany relationship with BeerOrder entity
- [x] 1.4. Add appropriate annotations (@Entity, @Table, etc.)
- [x] 1.5. Add Lombok annotations for convenience (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)

## 2. Update BeerOrder Entity
- [x] 2.1. Add ManyToOne relationship with Customer entity
- [x] 2.2. Add a reference to Customer entity
- [x] 2.3. Update the existing customerRef field to maintain backward compatibility if needed
- [x] 2.4. Add appropriate JPA annotations for the relationship

## 3. Create Customer DTO
- [x] 3.1. Create a new Java class `CustomerDto` in the `com.igorfragadev.juniemvc.models` package
- [x] 3.2. Implement all required properties, matching the Customer entity
- [x] 3.3. Add validation annotations (@NotNull, @NotBlank, etc.) for required fields
- [x] 3.4. Add Lombok annotations for convenience (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)

## 4. Update BeerOrderDto
- [x] 4.1. Add a reference to CustomerDto
- [x] 4.2. Update any related fields to maintain consistency with the BeerOrder entity

## 5. Create Customer Mapper
- [x] 5.1. Create a new Java interface `CustomerMapper` in the `com.igorfragadev.juniemvc.mappers` package
- [x] 5.2. Implement methods to map between Customer entity and CustomerDto
- [x] 5.3. Add appropriate MapStruct annotations

## 6. Update BeerOrderMapper
- [x] 6.1. Update to handle the new Customer relationship
- [x] 6.2. Ensure proper mapping between Customer and CustomerDto

## 7. Create Customer Repository
- [x] 7.1. Create a new Java interface `CustomerRepository` in the `com.igorfragadev.juniemvc.repositories` package
- [x] 7.2. Extend JpaRepository for basic CRUD operations
- [x] 7.3. Add any custom query methods if needed

## 8. Create Customer Service Interface
- [x] 8.1. Create a new Java interface `CustomerService` in the `com.igorfragadev.juniemvc.services` package
- [x] 8.2. Define methods for CRUD operations:
  - [x] 8.2.1. getAllCustomers
  - [x] 8.2.2. getCustomerById
  - [x] 8.2.3. saveCustomer
  - [x] 8.2.4. updateCustomer
  - [x] 8.2.5. deleteCustomer
  - [x] 8.2.6. Any other required business methods

## 9. Create Customer Service Implementation
- [x] 9.1. Create a new Java class `CustomerServiceImpl` in the `com.igorfragadev.juniemvc.services` package
- [x] 9.2. Implement the CustomerService interface
- [x] 9.3. Inject CustomerRepository and CustomerMapper
- [x] 9.4. Implement all required business logic

## 10. Create Customer Controller
- [x] 10.1. Create a new Java class `CustomerController` in the `com.igorfragadev.juniemvc.controllers` package
- [x] 10.2. Implement REST endpoints for CRUD operations:
  - [x] 10.2.1. GET /api/v1/customers - Get all customers
  - [x] 10.2.2. GET /api/v1/customers/{customerId} - Get customer by ID
  - [x] 10.2.3. POST /api/v1/customers - Create a new customer
  - [x] 10.2.4. PUT /api/v1/customers/{customerId} - Update an existing customer
  - [x] 10.2.5. DELETE /api/v1/customers/{customerId} - Delete a customer
- [x] 10.3. Add appropriate request mappings, response status codes, and validation

## 11. Create Flyway Migration
- [x] 11.1. Create a new SQL file `V2__create_customer_table.sql` in the `src/main/resources/db/migration` directory
- [x] 11.2. Implement SQL to create the customer table with all required fields
- [x] 11.3. Add SQL to alter the beer_order table to add a foreign key to the customer table

## 12. Update OpenAPI Documentation
- [x] 12.1. Add new schemas for Customer in the OpenAPI specification
- [x] 12.2. Add new paths for Customer endpoints
- [x] 12.3. Update existing schemas and paths as needed to reflect the new relationships

## 13. Create Tests
- [x] 13.1. Create unit tests for all new components:
  - [x] 13.1.1. CustomerRepository
  - [x] 13.1.2. CustomerMapper
  - [x] 13.1.3. CustomerService
  - [x] 13.1.4. CustomerController
- [x] 13.2. Update existing tests as needed to accommodate the new relationships
- [x] 13.3. Ensure all tests pass

## 14. Verify Implementation
- [x] 14.1. Run the application to ensure it starts without errors
- [x] 14.2. Test all new endpoints manually or with automated tests
- [x] 14.3. Verify that the database schema is updated correctly
- [x] 14.4. Ensure all requirements are met
- [x] 14.5. Ensure that all tests are passing and fix any errors that arise during testing
