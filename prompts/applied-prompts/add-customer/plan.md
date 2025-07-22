# Plan for Adding Customer Entity to the Project

## 1. Create Customer Entity
- Create a new Java class `Customer` in the `com.igorfragadev.juniemvc.entities` package
- Implement all required properties as specified in the requirements:
  - `id`: Integer, primary key, auto-generated
  - `version`: Integer, for optimistic locking
  - `name`: String, not null
  - `email`: String
  - `phone`: String
  - `addressLine1`: String, not null
  - `addressLine2`: String, optional
  - `city`: String, not null
  - `state`: String, not null
  - `postalCode`: String, not null
  - `createdAt`: LocalDateTime, auto-generated on creation
  - `updatedAt`: LocalDateTime, auto-updated on modification
- Add OneToMany relationship with BeerOrder entity
- Add appropriate annotations (@Entity, @Table, etc.)
- Add Lombok annotations for convenience (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)

## 2. Update BeerOrder Entity
- Add ManyToOne relationship with Customer entity
- Add a reference to Customer entity
- Update the existing customerRef field to maintain backward compatibility if needed
- Add appropriate JPA annotations for the relationship

## 3. Create Customer DTO
- Create a new Java class `CustomerDto` in the `com.igorfragadev.juniemvc.models` package
- Implement all required properties, matching the Customer entity
- Add validation annotations (@NotNull, @NotBlank, etc.) for required fields
- Add Lombok annotations for convenience (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)

## 4. Update BeerOrderDto
- Add a reference to CustomerDto
- Update any related fields to maintain consistency with the BeerOrder entity

## 5. Create Customer Mapper
- Create a new Java interface `CustomerMapper` in the `com.igorfragadev.juniemvc.mappers` package
- Implement methods to map between Customer entity and CustomerDto
- Add appropriate MapStruct annotations

## 6. Update BeerOrderMapper
- Update to handle the new Customer relationship
- Ensure proper mapping between Customer and CustomerDto

## 7. Create Customer Repository
- Create a new Java interface `CustomerRepository` in the `com.igorfragadev.juniemvc.repositories` package
- Extend JpaRepository for basic CRUD operations
- Add any custom query methods if needed

## 8. Create Customer Service Interface
- Create a new Java interface `CustomerService` in the `com.igorfragadev.juniemvc.services` package
- Define methods for CRUD operations:
  - getAllCustomers
  - getCustomerById
  - saveCustomer
  - updateCustomer
  - deleteCustomer
  - Any other required business methods

## 9. Create Customer Service Implementation
- Create a new Java class `CustomerServiceImpl` in the `com.igorfragadev.juniemvc.services` package
- Implement the CustomerService interface
- Inject CustomerRepository and CustomerMapper
- Implement all required business logic

## 10. Create Customer Controller
- Create a new Java class `CustomerController` in the `com.igorfragadev.juniemvc.controllers` package
- Implement REST endpoints for CRUD operations:
  - GET /api/v1/customers - Get all customers
  - GET /api/v1/customers/{customerId} - Get customer by ID
  - POST /api/v1/customers - Create a new customer
  - PUT /api/v1/customers/{customerId} - Update an existing customer
  - DELETE /api/v1/customers/{customerId} - Delete a customer
- Add appropriate request mappings, response status codes, and validation

## 11. Create Flyway Migration
- Create a new SQL file `V2__create_customer_table.sql` in the `src/main/resources/db/migration` directory
- Implement SQL to create the customer table with all required fields
- Add SQL to alter the beer_order table to add a foreign key to the customer table

## 12. Update OpenAPI Documentation
- Add new schemas for Customer in the OpenAPI specification
- Add new paths for Customer endpoints
- Update existing schemas and paths as needed to reflect the new relationships

## 13. Create Tests
- Create unit tests for all new components:
  - CustomerRepository
  - CustomerMapper
  - CustomerService
  - CustomerController
- Update existing tests as needed to accommodate the new relationships
- Ensure all tests pass

## 14. Verify Implementation
- Run the application to ensure it starts without errors
- Test all new endpoints manually or with automated tests
- Verify that the database schema is updated correctly
- Ensure all requirements are met