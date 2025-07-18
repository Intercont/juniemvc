package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderShipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerOrderShipmentRepositoryTest {

    @Autowired
    BeerOrderShipmentRepository beerOrderShipmentRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;
    BeerOrder testBeerOrder;
    BeerOrderShipment testBeerOrderShipment;

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
    void testSaveBeerOrderShipment() {
        // Given
        BeerOrderShipment beerOrderShipment = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        testBeerOrder.addBeerOrderShipment(beerOrderShipment);

        // When
        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);

        // Then
        assertThat(savedBeerOrderShipment).isNotNull();
        assertThat(savedBeerOrderShipment.getId()).isNotNull();
        assertThat(savedBeerOrderShipment.getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
        assertThat(savedBeerOrderShipment.getShipmentDate()).isEqualTo(beerOrderShipment.getShipmentDate());
        assertThat(savedBeerOrderShipment.getCarrier()).isEqualTo("UPS");
        assertThat(savedBeerOrderShipment.getTrackingNumber()).isEqualTo("1Z999AA10123456784");
    }

    @Test
    void testGetBeerOrderShipmentById() {
        // Given
        BeerOrderShipment beerOrderShipment = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        testBeerOrder.addBeerOrderShipment(beerOrderShipment);
        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);

        // When
        Optional<BeerOrderShipment> fetchedBeerOrderShipmentOptional = beerOrderShipmentRepository.findById(savedBeerOrderShipment.getId());

        // Then
        assertThat(fetchedBeerOrderShipmentOptional).isPresent();
        BeerOrderShipment fetchedBeerOrderShipment = fetchedBeerOrderShipmentOptional.get();
        assertThat(fetchedBeerOrderShipment.getShipmentDate()).isEqualTo(beerOrderShipment.getShipmentDate());
        assertThat(fetchedBeerOrderShipment.getCarrier()).isEqualTo("UPS");
        assertThat(fetchedBeerOrderShipment.getTrackingNumber()).isEqualTo("1Z999AA10123456784");
        assertThat(fetchedBeerOrderShipment.getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
    }

    @Test
    void testUpdateBeerOrderShipment() {
        // Given
        BeerOrderShipment beerOrderShipment = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        testBeerOrder.addBeerOrderShipment(beerOrderShipment);
        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);

        // When
        savedBeerOrderShipment.setCarrier("FedEx");
        savedBeerOrderShipment.setTrackingNumber("9876543210");
        BeerOrderShipment updatedBeerOrderShipment = beerOrderShipmentRepository.save(savedBeerOrderShipment);

        // Then
        assertThat(updatedBeerOrderShipment.getCarrier()).isEqualTo("FedEx");
        assertThat(updatedBeerOrderShipment.getTrackingNumber()).isEqualTo("9876543210");
    }

    @Test
    void testDeleteBeerOrderShipment() {
        // Given
        BeerOrderShipment beerOrderShipment = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        testBeerOrder.addBeerOrderShipment(beerOrderShipment);
        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);

        // When
        beerOrderShipmentRepository.delete(savedBeerOrderShipment);
        Optional<BeerOrderShipment> deletedBeerOrderShipment = beerOrderShipmentRepository.findById(savedBeerOrderShipment.getId());

        // Then
        assertThat(deletedBeerOrderShipment).isEmpty();
    }

    @Test
    void testFindByBeerOrderId() {
        // Given
        BeerOrderShipment beerOrderShipment1 = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        BeerOrderShipment beerOrderShipment2 = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        testBeerOrder.addBeerOrderShipment(beerOrderShipment1);
        testBeerOrder.addBeerOrderShipment(beerOrderShipment2);
        
        beerOrderShipmentRepository.save(beerOrderShipment1);
        beerOrderShipmentRepository.save(beerOrderShipment2);

        // When
        List<BeerOrderShipment> shipments = beerOrderShipmentRepository.findByBeerOrderId(testBeerOrder.getId());

        // Then
        assertThat(shipments).isNotNull();
        assertThat(shipments).hasSize(2);
        assertThat(shipments.get(0).getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
        assertThat(shipments.get(1).getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
    }
}