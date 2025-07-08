package com.igorfragadev.juniemvc.migration;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import com.igorfragadev.juniemvc.repositories.BeerOrderLineRepository;
import com.igorfragadev.juniemvc.repositories.BeerOrderRepository;
import com.igorfragadev.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FlywayMigrationTest {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private BeerOrderLineRepository beerOrderLineRepository;

    @Test
    @Transactional
    void testMigration() {
        // Create and save a Beer
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .quantityOnHand(100)
                .price(new BigDecimal("10.99"))
                .build();
        Beer savedBeer = beerRepository.save(beer);
        assertNotNull(savedBeer.getId());

        // Create and save a BeerOrder
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test Customer")
                .paymentAmount(new BigDecimal("21.98"))
                .status("NEW")
                .build();
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        assertNotNull(savedBeerOrder.getId());

        // Create and save a BeerOrderLine
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beer(savedBeer)
                .beerOrder(savedBeerOrder)
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .build();
        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);
        assertNotNull(savedBeerOrderLine.getId());

        // Verify relationships
        List<BeerOrderLine> orderLines = beerOrderLineRepository.findAll();
        assertEquals(1, orderLines.size());
        assertEquals(savedBeer.getId(), orderLines.get(0).getBeer().getId());
        assertEquals(savedBeerOrder.getId(), orderLines.get(0).getBeerOrder().getId());
    }
}