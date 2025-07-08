package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;
    BeerOrder testBeerOrder;

    @BeforeEach
    void setUp() {
        // Create and save a test beer
        testBeer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testBeer = beerRepository.save(testBeer);
    }

    @Test
    void testSaveBeerOrder() {
        // Given
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();

        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        beerOrder.addBeerOrderLine(beerOrderLine);

        // When
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        // Then
        assertThat(savedBeerOrder).isNotNull();
        assertThat(savedBeerOrder.getId()).isNotNull();
        assertThat(savedBeerOrder.getBeerOrderLines()).hasSize(1);
        assertThat(savedBeerOrder.getBeerOrderLines().get(0).getBeer().getId()).isEqualTo(testBeer.getId());
    }

    @Test
    void testGetBeerOrderById() {
        // Given
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();

        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        beerOrder.addBeerOrderLine(beerOrderLine);
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        // When
        Optional<BeerOrder> fetchedBeerOrderOptional = beerOrderRepository.findById(savedBeerOrder.getId());

        // Then
        assertThat(fetchedBeerOrderOptional).isPresent();
        BeerOrder fetchedBeerOrder = fetchedBeerOrderOptional.get();
        assertThat(fetchedBeerOrder.getCustomerRef()).isEqualTo("TEST-REF-001");
        assertThat(fetchedBeerOrder.getBeerOrderLines()).hasSize(1);
        assertThat(fetchedBeerOrder.getBeerOrderLines().get(0).getBeer().getBeerName()).isEqualTo("Test Beer");
    }

    @Test
    void testUpdateBeerOrder() {
        // Given
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();

        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        beerOrder.addBeerOrderLine(beerOrderLine);
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        // When
        savedBeerOrder.setCustomerRef("UPDATED-REF-001");
        savedBeerOrder.setStatus("UPDATED");
        BeerOrder updatedBeerOrder = beerOrderRepository.save(savedBeerOrder);

        // Then
        assertThat(updatedBeerOrder.getCustomerRef()).isEqualTo("UPDATED-REF-001");
        assertThat(updatedBeerOrder.getStatus()).isEqualTo("UPDATED");
    }

    @Test
    void testDeleteBeerOrder() {
        // Given
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();

        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        beerOrder.addBeerOrderLine(beerOrderLine);
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        // When
        beerOrderRepository.delete(savedBeerOrder);
        Optional<BeerOrder> deletedBeerOrder = beerOrderRepository.findById(savedBeerOrder.getId());

        // Then
        assertThat(deletedBeerOrder).isEmpty();
    }

    @Test
    void testListBeerOrders() {
        // Given
        BeerOrder beerOrder1 = BeerOrder.builder()
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();

        BeerOrderLine beerOrderLine1 = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        beerOrder1.addBeerOrderLine(beerOrderLine1);
        
        BeerOrder beerOrder2 = BeerOrder.builder()
                .customerRef("TEST-REF-002")
                .paymentAmount(new BigDecimal("259.80"))
                .status("NEW")
                .build();

        BeerOrderLine beerOrderLine2 = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(20)
                .quantityAllocated(10)
                .status("NEW")
                .build();

        beerOrder2.addBeerOrderLine(beerOrderLine2);
        
        beerOrderRepository.save(beerOrder1);
        beerOrderRepository.save(beerOrder2);

        // When
        List<BeerOrder> beerOrders = beerOrderRepository.findAll();

        // Then
        assertThat(beerOrders).isNotNull();
        assertThat(beerOrders.size()).isGreaterThanOrEqualTo(2);
    }
}