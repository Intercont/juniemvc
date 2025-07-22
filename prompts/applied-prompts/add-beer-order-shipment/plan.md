# Plan for Adding BeerOrderShipment to the Project

## Overview
This plan outlines the steps required to add a new entity called `BeerOrderShipment` to the project. The `BeerOrderShipment` entity will have a ManyToOne relationship with the `BeerOrder` entity, allowing a beer order to have multiple shipments.

## 1. Create Flyway Migration
Create a new Flyway migration script to add the `beer_order_shipment` table to the database.

The migration script will create a new table called `beer_order_shipment` with the following columns:
- `id`: A unique identifier for the shipment (auto-generated)
- `beer_order_id`: A foreign key reference to the beer_order table
- `shipment_date`: The date of the shipment
- `carrier`: The carrier of the shipment
- `tracking_number`: The tracking number of the shipment
- `created_at`: The timestamp when the record was created
- `updated_at`: The timestamp when the record was last updated

The script will also add a foreign key constraint to ensure that each shipment is associated with a valid beer order.

The SQL script will be named `V3__create_beer_order_shipment_table.sql` and placed in the `src/main/resources/db/migration` directory.

## 2. Create Entity
Create a new JPA entity class called `BeerOrderShipment` with the required fields and relationships.

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beer_order_shipment")
public class BeerOrderShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_order_id")
    private BeerOrder beerOrder;

    private LocalDate shipmentDate;

    private String carrier;

    private String trackingNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

## 3. Update BeerOrder Entity
Update the `BeerOrder` entity to include a OneToMany relationship with `BeerOrderShipment`.

```java
@OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@Builder.Default
private List<BeerOrderShipment> beerOrderShipments = new ArrayList<>();

// Helper method to maintain bidirectional relationship
public void addBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
    beerOrderShipments.add(beerOrderShipment);
    beerOrderShipment.setBeerOrder(this);
}
```

## 4. Create DTO
Create a DTO for the `BeerOrderShipment` entity.

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderShipmentDto {
    // read only
    private Integer id;

    private Integer beerOrderId;

    private LocalDate shipmentDate;

    private String carrier;

    private String trackingNumber;

    // read only
    private LocalDateTime createdAt;
    // read only
    private LocalDateTime updatedAt;
}
```

## 5. Create Mapper
Create a mapper interface to convert between `BeerOrderShipment` entity and `BeerOrderShipmentDto`.

```java
@Mapper(componentModel = "spring")
public interface BeerOrderShipmentMapper {
    BeerOrderShipmentDto beerOrderShipmentToBeerOrderShipmentDto(BeerOrderShipment beerOrderShipment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);
}
```

## 6. Create Repository
Create a repository interface for the `BeerOrderShipment` entity.

```java
public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, Integer> {
    List<BeerOrderShipment> findByBeerOrderId(Integer beerOrderId);
}
```

## 7. Create Service Interface
Create a service interface for the `BeerOrderShipment` entity.

```java
public interface BeerOrderShipmentService {
    /**
     * Get all beer order shipments
     * @return List of all beer order shipments
     */
    List<BeerOrderShipmentDto> getAllBeerOrderShipments();

    /**
     * Get all shipments for a specific beer order
     * @param beerOrderId The beer order ID
     * @return List of shipments for the beer order
     */
    List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId);

    /**
     * Get a beer order shipment by its ID
     * @param id The beer order shipment ID
     * @return Optional containing the beer order shipment if found
     */
    Optional<BeerOrderShipmentDto> getBeerOrderShipmentById(Integer id);

    /**
     * Save a new beer order shipment
     * @param beerOrderShipmentDto The beer order shipment to save
     * @return The saved beer order shipment
     */
    BeerOrderShipmentDto saveBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);

    /**
     * Update an existing beer order shipment
     * @param id The beer order shipment ID
     * @param beerOrderShipmentDto The updated beer order shipment data
     * @return Optional containing the updated beer order shipment if found
     */
    Optional<BeerOrderShipmentDto> updateBeerOrderShipment(Integer id, BeerOrderShipmentDto beerOrderShipmentDto);

    /**
     * Delete a beer order shipment by its ID
     * @param id The beer order shipment ID
     * @return true if the beer order shipment was deleted, false if not found
     */
    boolean deleteBeerOrderShipment(Integer id);
}
```

## 8. Create Service Implementation
Create a service implementation for the `BeerOrderShipment` entity.

```java
@Service
public class BeerOrderShipmentServiceImpl implements BeerOrderShipmentService {

    private final BeerOrderShipmentRepository beerOrderShipmentRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderShipmentMapper beerOrderShipmentMapper;

    public BeerOrderShipmentServiceImpl(BeerOrderShipmentRepository beerOrderShipmentRepository,
                                       BeerOrderRepository beerOrderRepository,
                                       BeerOrderShipmentMapper beerOrderShipmentMapper) {
        this.beerOrderShipmentRepository = beerOrderShipmentRepository;
        this.beerOrderRepository = beerOrderRepository;
        this.beerOrderShipmentMapper = beerOrderShipmentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getAllBeerOrderShipments() {
        return beerOrderShipmentRepository.findAll().stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId) {
        return beerOrderShipmentRepository.findByBeerOrderId(beerOrderId).stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderShipmentDto> getBeerOrderShipmentById(Integer id) {
        return beerOrderShipmentRepository.findById(id)
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto);
    }

    @Override
    @Transactional
    public BeerOrderShipmentDto saveBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto) {
        BeerOrder beerOrder = beerOrderRepository.findById(beerOrderShipmentDto.getBeerOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Beer Order not found with ID: " + beerOrderShipmentDto.getBeerOrderId()));

        BeerOrderShipment beerOrderShipment = beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(beerOrderShipmentDto);
        beerOrder.addBeerOrderShipment(beerOrderShipment);

        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);
        return beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(savedBeerOrderShipment);
    }

    @Override
    @Transactional
    public Optional<BeerOrderShipmentDto> updateBeerOrderShipment(Integer id, BeerOrderShipmentDto beerOrderShipmentDto) {
        return beerOrderShipmentRepository.findById(id)
                .map(existingBeerOrderShipment -> {
                    // Update basic fields
                    existingBeerOrderShipment.setShipmentDate(beerOrderShipmentDto.getShipmentDate());
                    existingBeerOrderShipment.setCarrier(beerOrderShipmentDto.getCarrier());
                    existingBeerOrderShipment.setTrackingNumber(beerOrderShipmentDto.getTrackingNumber());

                    // If beer order ID has changed, update the relationship
                    if (!existingBeerOrderShipment.getBeerOrder().getId().equals(beerOrderShipmentDto.getBeerOrderId())) {
                        BeerOrder newBeerOrder = beerOrderRepository.findById(beerOrderShipmentDto.getBeerOrderId())
                                .orElseThrow(() -> new EntityNotFoundException("Beer Order not found with ID: " + beerOrderShipmentDto.getBeerOrderId()));
                        existingBeerOrderShipment.setBeerOrder(newBeerOrder);
                    }

                    return beerOrderShipmentRepository.save(existingBeerOrderShipment);
                })
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto);
    }

    @Override
    @Transactional
    public boolean deleteBeerOrderShipment(Integer id) {
        if (beerOrderShipmentRepository.existsById(id)) {
            beerOrderShipmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

## 9. Create Controller
Create a controller for the `BeerOrderShipment` entity.

```java
@RestController
@RequestMapping("/api/v1/beer-order-shipments")
public class BeerOrderShipmentController {

    private final BeerOrderShipmentService beerOrderShipmentService;

    public BeerOrderShipmentController(BeerOrderShipmentService beerOrderShipmentService) {
        this.beerOrderShipmentService = beerOrderShipmentService;
    }

    @GetMapping
    public ResponseEntity<List<BeerOrderShipmentDto>> getAllBeerOrderShipments() {
        return new ResponseEntity<>(beerOrderShipmentService.getAllBeerOrderShipments(), HttpStatus.OK);
    }

    @GetMapping("/beer-order/{beerOrderId}")
    public ResponseEntity<List<BeerOrderShipmentDto>> getShipmentsByBeerOrderId(@PathVariable("beerOrderId") Integer beerOrderId) {
        return new ResponseEntity<>(beerOrderShipmentService.getShipmentsByBeerOrderId(beerOrderId), HttpStatus.OK);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<BeerOrderShipmentDto> getBeerOrderShipmentById(@PathVariable("shipmentId") Integer shipmentId) {
        return beerOrderShipmentService.getBeerOrderShipmentById(shipmentId)
                .map(shipment -> new ResponseEntity<>(shipment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BeerOrderShipmentDto> createBeerOrderShipment(@Valid @RequestBody BeerOrderShipmentDto beerOrderShipmentDto) {
        BeerOrderShipmentDto savedShipment = beerOrderShipmentService.saveBeerOrderShipment(beerOrderShipmentDto);
        return new ResponseEntity<>(savedShipment, HttpStatus.CREATED);
    }

    @PutMapping("/{shipmentId}")
    public ResponseEntity<BeerOrderShipmentDto> updateBeerOrderShipment(
            @PathVariable("shipmentId") Integer shipmentId,
            @Valid @RequestBody BeerOrderShipmentDto beerOrderShipmentDto) {
        return beerOrderShipmentService.updateBeerOrderShipment(shipmentId, beerOrderShipmentDto)
                .map(updatedShipment -> new ResponseEntity<>(updatedShipment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteBeerOrderShipment(@PathVariable("shipmentId") Integer shipmentId) {
        if (beerOrderShipmentService.deleteBeerOrderShipment(shipmentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
```

## 10. Create Tests
Create tests for all components:

1. Entity tests
2. Repository tests
3. Mapper tests
4. Service tests
5. Controller tests

## 11. Update OpenAPI Documentation
Update the OpenAPI documentation to include the new endpoints for the `BeerOrderShipment` entity.

## 12. Validate and Test
1. Run all tests to ensure everything is working correctly
2. Validate the OpenAPI documentation with the linting tool
3. Fix any issues that arise during testing and validation
