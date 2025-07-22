# Analyze the attached ERD diagram image.

Create detailed instructions for a Java developer to implement the relationships in JPA with Lombok.

This ERD diagram shows a beer ordering system with three main entities: **BeerOrder**, **BeerOrderLine**, and **Beer**. The relationships demonstrate a classic order management pattern where orders contain multiple line items, and each line item references a specific beer product.

## **Entity Relationship Analysis**

The diagram shows two key relationships:

- **BeerOrder** to **BeerOrderLine**: One-to-Many relationship (one order can have multiple line items)
- **Beer** to **BeerOrderLine**: One-to-Many relationship (one beer can appear in multiple order lines)


## **JPA Entity Implementation Instructions**

### **BeerOrder Entity**

Create the parent entity with a bidirectional one-to-many relationship to BeerOrderLine:

```java
@Entity
@Table(name = "beer_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Version
    private Integer version;
    
    @Column(name = "customer_ref")
    private String customerRef;
    
    @Column(name = "payment_amount", precision = 19, scale = 2)
    private BigDecimal paymentAmount;
    
    private String status;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    
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


### **Beer Entity**

Create the beer entity with a one-to-many relationship to BeerOrderLine:

```java
@Entity
@Table(name = "beer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Version
    private Integer version;
    
    @Column(name = "beer_name")
    private String beerName;
    
    @Column(name = "beer_style")
    private String beerStyle;
    
    private String upc;
    
    @Column(name = "quantity_on_hand")
    private Integer quantityOnHand;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal price;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    
    @OneToMany(mappedBy = "beer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<BeerOrderLine> beerOrderLines = new ArrayList<>();
}
```


### **BeerOrderLine Entity**

Create the junction entity that connects BeerOrder and Beer:

```java
@Entity
@Table(name = "beer_order_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"beerOrder", "beer"})
@ToString(exclude = {"beerOrder", "beer"})
public class BeerOrderLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Version
    private Integer version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_order_id", nullable = false)
    private BeerOrder beerOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_id", nullable = false)
    private Beer beer;
    
    @Column(name = "order_quantity")
    private Integer orderQuantity;
    
    @Column(name = "quantity_allocated")
    private Integer quantityAllocated;
    
    private String status;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
```


## **Key Implementation Considerations**

### **Lombok Annotations**

- Use `@Data` for automatic getter/setter generation
- Include `@NoArgsConstructor` and `@AllArgsConstructor` for JPA compatibility
- Add `@Builder` for convenient object creation
- Use `@EqualsAndHashCode(exclude = {...})` and `@ToString(exclude = {...})` on BeerOrderLine to prevent circular references


### **JPA Relationship Mapping**

- **@OneToMany**: Use `mappedBy` to indicate the owning side of the relationship
- **@ManyToOne**: Use `@JoinColumn` to specify the foreign key column
- Set `fetch = FetchType.LAZY` for performance optimization
- Use `cascade = CascadeType.ALL` on BeerOrder to automatically persist/update order lines


### **Bidirectional Relationship Management**

- Implement helper methods like `addBeerOrderLine()` to maintain consistency
- Initialize collections with `@Builder.Default` to prevent null pointer exceptions
- Exclude relationship fields from `equals()` and `toString()` methods to avoid infinite loops


### **Database Considerations**

- Use `@Version` for optimistic locking
- Specify precision and scale for `BigDecimal` fields
- Map `LocalDateTime` fields for audit timestamps
- Use appropriate column names with `@Column(name = "...")` for database compatibility

This implementation provides a robust foundation for managing beer orders with proper JPA relationships and Lombok integration for reduced boilerplate code.

## **Implementing the newly created Entities**
Create the new entities in the project structure
Analyze the implementation of BeerController, BeerService, and BeerRepository to understand how they work and create
new controllers, services, repositories, DTOs, mappers, and tests for these new entities, BeerOrderLine and BeerOrder.
Ensure that the new entities are properly integrated into the existing application flow, including CRUD operations and any necessary business logic.
