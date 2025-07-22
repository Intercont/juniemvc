## Change Requirements
Add a new entity to the project called `Customer`. The `Customer` entity should have the following properties:
- `id`: A unique identifier for the customer. Not null.
- `name`: The name of the customer. Not null.
- `email`: The email address of the customer.
- `phone`: The phone number of the customer.
- address line 1: The address of the customer. Not Null.
- address line 2: An optional second line for the address.
- `city`: The city where the customer resides. Not null.
- `state`: The state where the customer resides. Not null.
- `postalCode`: The postal code for the customer's address. Not null.
- `createdAt`: The date and time when the customer was created.
- `updatedAt`: The date and time when the customer was last updated.

The Customer entity has a OneToMany relationship with BeerOrder entity.

Add a flyway migration to create the `Customer` table in the database with the specified properties.

Add Java DTOs, Mappers, Spring Data JPA repositories, service and service implementation to support a full CRUD 
(Create, Read, Update, Delete) operations for the `Customer` entity. Add tests for all components. Update the OpenAPI 
documentation for the new controller operations. Verify all tests are passing.