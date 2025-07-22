## Change Requirements
Add a new entity to the project called `BeerOrderShipment`. The `BeerOrderShipment` entity should have the following properties:
- `id`: A unique identifier for the shipment. Not null.
- `shipmentDate`: The date of the shipment.
- `carrier`: The carrier of the shipment.
- `trackingNumber`: The tracking number of the shipment.
- `createdAt`: The date and time when the shipment was created.
- `updatedAt`: The date and time when the shipment was last updated.

The BeerOrderShipment entity has a ManyToOne relationship with the BeerOrder entity.

Add a flyway migration to create the `BeerOrderShipment` table in the database with the specified properties.

Add Java DTOs, Mappers, Spring Data JPA repositories, service and service implementation to support a full CRUD 
(Create, Read, Update, Delete) operations for the `BeerOrderShipment` entity. 
Add tests for all components.
Verify all tests are passing and fix what is needed until they are all passing.
Update the OpenAPI documentation for the new controller operations. 
Validate the documentation with the linting tool.
