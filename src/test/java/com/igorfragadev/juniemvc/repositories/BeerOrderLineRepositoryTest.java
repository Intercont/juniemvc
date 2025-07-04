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
class BeerOrderLineRepositoryTest {

    @Autowired
    BeerOrderLineRepository beerOrderLineRepository;

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

        // Create and save a test beer order
        testBeerOrder = BeerOrder.builder()
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();
        testBeerOrder = beerOrderRepository.save(testBeerOrder);
    }

    @Test
    void testSaveBeerOrderLine() {
        // Given
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        // When
        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);

        // Then
        assertThat(savedBeerOrderLine).isNotNull();
        assertThat(savedBeerOrderLine.getId()).isNotNull();
        assertThat(savedBeerOrderLine.getBeer().getId()).isEqualTo(testBeer.getId());
        assertThat(savedBeerOrderLine.getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
    }

    @Test
    void testGetBeerOrderLineById() {
        // Given
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();
        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);

        // When
        Optional<BeerOrderLine> fetchedBeerOrderLineOptional = beerOrderLineRepository.findById(savedBeerOrderLine.getId());

        // Then
        assertThat(fetchedBeerOrderLineOptional).isPresent();
        BeerOrderLine fetchedBeerOrderLine = fetchedBeerOrderLineOptional.get();
        assertThat(fetchedBeerOrderLine.getOrderQuantity()).isEqualTo(10);
        assertThat(fetchedBeerOrderLine.getBeer().getBeerName()).isEqualTo("Test Beer");
        assertThat(fetchedBeerOrderLine.getBeerOrder().getCustomerRef()).isEqualTo("TEST-REF-001");
    }

    @Test
    void testUpdateBeerOrderLine() {
        // Given
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();
        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);

        // When
        savedBeerOrderLine.setOrderQuantity(15);
        savedBeerOrderLine.setQuantityAllocated(10);
        savedBeerOrderLine.setStatus("UPDATED");
        BeerOrderLine updatedBeerOrderLine = beerOrderLineRepository.save(savedBeerOrderLine);

        // Then
        assertThat(updatedBeerOrderLine.getOrderQuantity()).isEqualTo(15);
        assertThat(updatedBeerOrderLine.getQuantityAllocated()).isEqualTo(10);
        assertThat(updatedBeerOrderLine.getStatus()).isEqualTo("UPDATED");
    }

    @Test
    void testDeleteBeerOrderLine() {
        // Given
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();
        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);

        // When
        beerOrderLineRepository.delete(savedBeerOrderLine);
        Optional<BeerOrderLine> deletedBeerOrderLine = beerOrderLineRepository.findById(savedBeerOrderLine.getId());

        // Then
        assertThat(deletedBeerOrderLine).isEmpty();
    }

    @Test
    void testListBeerOrderLines() {
        // Given
        BeerOrderLine beerOrderLine1 = BeerOrderLine.builder()
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();
        
        BeerOrderLine beerOrderLine2 = BeerOrderLine.builder()
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(20)
                .quantityAllocated(10)
                .status("NEW")
                .build();
        
        beerOrderLineRepository.save(beerOrderLine1);
        beerOrderLineRepository.save(beerOrderLine2);

        // When
        List<BeerOrderLine> beerOrderLines = beerOrderLineRepository.findAll();

        // Then
        assertThat(beerOrderLines).isNotNull();
        assertThat(beerOrderLines.size()).isGreaterThanOrEqualTo(2);
    }
}