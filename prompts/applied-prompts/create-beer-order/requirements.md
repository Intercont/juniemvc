# Beer Order System Implementation Requirements

## Overview
This document provides detailed requirements for implementing a beer ordering system with three main entities: **BeerOrder**, **BeerOrderLine**, and **Beer**. The implementation should follow the existing project patterns and Spring Boot best practices.

## Entity Relationships
The system has two key relationships:
- **BeerOrder** to **BeerOrderLine**: One-to-Many relationship (one order can have multiple line items)
- **Beer** to **BeerOrderLine**: One-to-Many relationship (one beer can appear in multiple order lines)

## Entity Implementation Requirements

### 1. Entity Classes

#### BeerOrder Entity
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BeerOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Version
    private Integer version;
    
    private String customerRef;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal paymentAmount;
    
    private String status;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BeerOrderLine> beerOrderLines = new ArrayList<>();
    
    // Helper method to maintain bidirectional relationship
    public void addBeerOrderLine(BeerOrderLine beerOrderLine) {
        beerOrderLines.add(beerOrderLine);
        beerOrderLine.setBeerOrder(this);
    }
}
```

#### BeerOrderLine Entity
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BeerOrderLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Version
    private Integer version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private BeerOrder beerOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Beer beer;
    
    private Integer orderQuantity;
    private Integer quantityAllocated;
    private String status;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
```

#### Beer Entity Updates
Update the existing Beer entity to include the relationship with BeerOrderLine:

```java
// Add to existing Beer entity
@OneToMany(mappedBy = "beer", fetch = FetchType.LAZY)
@Builder.Default
private List<BeerOrderLine> beerOrderLines = new ArrayList<>();
```

### 2. DTO Classes

#### BeerOrderDto
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderDto {
    private Integer id;
    private Integer version;
    
    @NotBlank(message = "Customer reference is required")
    private String customerRef;
    
    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be positive")
    private BigDecimal paymentAmount;
    
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    @Builder.Default
    private List<BeerOrderLineDto> beerOrderLines = new ArrayList<>();
}
```

#### BeerOrderLineDto
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderLineDto {
    private Integer id;
    private Integer version;
    
    @NotNull(message = "Beer ID is required")
    private Integer beerId;
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be positive")
    private Integer orderQuantity;
    
    private Integer quantityAllocated;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    // Optional: Include beer details for display purposes
    private BeerDto beer;
}
```

### 3. Mapper Interfaces

#### BeerOrderMapper
```java
@Mapper(componentModel = "spring", uses = {BeerOrderLineMapper.class})
public interface BeerOrderMapper {
    BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
}
```

#### BeerOrderLineMapper
```java
@Mapper(componentModel = "spring", uses = {BeerMapper.class})
public interface BeerOrderLineMapper {
    @Mapping(target = "beerId", source = "beer.id")
    BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beer", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}
```

### 4. Repository Interfaces

#### BeerOrderRepository
```java
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // Add custom query methods as needed
}
```

#### BeerOrderLineRepository
```java
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // Add custom query methods as needed
}
```

### 5. Service Layer

#### BeerOrderService Interface
```java
public interface BeerOrderService {
    /**
     * Get all beer orders
     * @return List of all beer orders
     */
    List<BeerOrderDto> getAllBeerOrders();

    /**
     * Get a beer order by its ID
     * @param id The beer order ID
     * @return Optional containing the beer order if found
     */
    Optional<BeerOrderDto> getBeerOrderById(Integer id);

    /**
     * Save a new beer order
     * @param beerOrderDto The beer order to save
     * @return The saved beer order
     */
    BeerOrderDto saveBeerOrder(BeerOrderDto beerOrderDto);

    /**
     * Update an existing beer order
     * @param id The beer order ID
     * @param beerOrderDto The updated beer order data
     * @return Optional containing the updated beer order if found
     */
    Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto);

    /**
     * Delete a beer order by its ID
     * @param id The beer order ID
     * @return true if the beer order was deleted, false if not found
     */
    boolean deleteBeerOrder(Integer id);
}
```

#### BeerOrderServiceImpl
```java
@Service
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;

    public BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository, 
                               BeerRepository beerRepository,
                               BeerOrderMapper beerOrderMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderRepository.findAll().stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    @Transactional
    public BeerOrderDto saveBeerOrder(BeerOrderDto beerOrderDto) {
        BeerOrder beerOrder = beerOrderMapper.beerOrderDtoToBeerOrder(beerOrderDto);
        
        // Process beer order lines
        if (beerOrderDto.getBeerOrderLines() != null) {
            beerOrderDto.getBeerOrderLines().forEach(lineDto -> {
                Beer beer = beerRepository.findById(lineDto.getBeerId())
                    .orElseThrow(() -> new EntityNotFoundException("Beer not found with ID: " + lineDto.getBeerId()));
                
                BeerOrderLine line = BeerOrderLine.builder()
                    .beer(beer)
                    .orderQuantity(lineDto.getOrderQuantity())
                    .quantityAllocated(0) // Default to 0 for new orders
                    .status("NEW")
                    .build();
                
                beerOrder.addBeerOrderLine(line);
            });
        }
        
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Override
    @Transactional
    public Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto) {
        return beerOrderRepository.findById(id)
                .map(existingBeerOrder -> {
                    // Update basic fields
                    existingBeerOrder.setCustomerRef(beerOrderDto.getCustomerRef());
                    existingBeerOrder.setPaymentAmount(beerOrderDto.getPaymentAmount());
                    existingBeerOrder.setStatus(beerOrderDto.getStatus());
                    
                    // Handle order lines updates if needed
                    // This is a simplified approach - in a real application, you might need
                    // more sophisticated logic to handle adding/removing/updating lines
                    
                    return beerOrderRepository.save(existingBeerOrder);
                })
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    @Transactional
    public boolean deleteBeerOrder(Integer id) {
        if (beerOrderRepository.existsById(id)) {
            beerOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

### 6. Controller Layer

#### BeerOrderController
```java
@RestController
@RequestMapping("/api/v1/beer-orders")
public class BeerOrderController {

    private final BeerOrderService beerOrderService;

    public BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @GetMapping
    public ResponseEntity<List<BeerOrderDto>> getAllBeerOrders() {
        return new ResponseEntity<>(beerOrderService.getAllBeerOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable("orderId") Integer orderId) {
        return beerOrderService.getBeerOrderById(orderId)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BeerOrderDto> createBeerOrder(@Valid @RequestBody BeerOrderDto beerOrderDto) {
        BeerOrderDto savedOrder = beerOrderService.saveBeerOrder(beerOrderDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<BeerOrderDto> updateBeerOrder(
            @PathVariable("orderId") Integer orderId,
            @Valid @RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.updateBeerOrder(orderId, beerOrderDto)
                .map(updatedOrder -> new ResponseEntity<>(updatedOrder, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteBeerOrder(@PathVariable("orderId") Integer orderId) {
        if (beerOrderService.deleteBeerOrder(orderId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
```

## Implementation Guidelines

### 1. Entity Relationships
- Use bidirectional relationships with proper mappings
- Implement helper methods to maintain relationship consistency
- Use lazy loading for collections to optimize performance
- Apply cascading operations appropriately

### 2. Data Transfer Objects (DTOs)
- Create dedicated DTOs for each entity
- Apply validation constraints with meaningful error messages
- Use DTOs for all controller request/response operations
- Include only necessary fields in DTOs to minimize data transfer

### 3. Mappers
- Use MapStruct for efficient object mapping
- Configure mappers with appropriate component model
- Ignore fields that should not be mapped (e.g., IDs for new entities)
- Handle nested object mappings properly

### 4. Transaction Management
- Apply @Transactional to service methods
- Use readOnly=true for query-only operations
- Ensure proper transaction boundaries

### 5. Exception Handling
- Use the existing GlobalExceptionHandler for consistent error responses
- Add specific exception handlers for new business exceptions
- Return appropriate HTTP status codes

### 6. Testing
- Write unit tests for all new components
- Create integration tests for repository and controller layers
- Use test data builders for consistent test data

## Implementation Steps
1. Create the entity classes
2. Create the repository interfaces
3. Create the DTO classes
4. Create the mapper interfaces
5. Create the service interfaces and implementations
6. Create the controllers
7. Write unit and integration tests
8. Update the existing Beer entity to include the relationship with BeerOrderLine

By following these requirements and guidelines, you will implement a robust beer ordering system that integrates well with the existing application architecture and follows Spring Boot best practices.