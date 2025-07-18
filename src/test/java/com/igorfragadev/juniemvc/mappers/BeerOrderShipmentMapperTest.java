package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderShipment;
import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BeerOrderShipmentMapperTest {

    private final BeerOrderShipmentMapper beerOrderShipmentMapper = Mappers.getMapper(BeerOrderShipmentMapper.class);

    private BeerOrderShipment testBeerOrderShipment;
    private BeerOrderShipmentDto testBeerOrderShipmentDto;
    private BeerOrder testBeerOrder;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        // Create test beer order
        testBeerOrder = BeerOrder.builder()
                .id(1)
                .version(1)
                .customerRef("TEST-REF-001")
                .status("NEW")
                .build();

        // Create test beer order shipment
        testBeerOrderShipment = BeerOrderShipment.builder()
                .id(1)
                .version(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(today)
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .createdDate(now)
                .updatedDate(now)
                .build();

        // Create test beer order shipment DTO
        testBeerOrderShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .version(1)
                .beerOrderId(1)
                .shipmentDate(today)
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .createdDate(now)
                .updatedDate(now)
                .build();
    }

    @Test
    void beerOrderShipmentToBeerOrderShipmentDto() {
        // when
        BeerOrderShipmentDto dto = beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(testBeerOrderShipment.getId());
        assertThat(dto.getVersion()).isEqualTo(testBeerOrderShipment.getVersion());
        assertThat(dto.getBeerOrderId()).isEqualTo(testBeerOrderShipment.getBeerOrder().getId());
        assertThat(dto.getShipmentDate()).isEqualTo(testBeerOrderShipment.getShipmentDate());
        assertThat(dto.getCarrier()).isEqualTo(testBeerOrderShipment.getCarrier());
        assertThat(dto.getTrackingNumber()).isEqualTo(testBeerOrderShipment.getTrackingNumber());
        assertThat(dto.getCreatedDate()).isEqualTo(testBeerOrderShipment.getCreatedDate());
        assertThat(dto.getUpdatedDate()).isEqualTo(testBeerOrderShipment.getUpdatedDate());
    }

    @Test
    void beerOrderShipmentDtoToBeerOrderShipment() {
        // when
        BeerOrderShipment entity = beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(testBeerOrderShipmentDto);

        // then
        assertThat(entity).isNotNull();
        // id, createdDate, updatedDate, and beerOrder should be ignored
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedDate()).isNull();
        assertThat(entity.getUpdatedDate()).isNull();
        assertThat(entity.getBeerOrder()).isNull();

        // other fields should be mapped
        assertThat(entity.getVersion()).isEqualTo(testBeerOrderShipmentDto.getVersion());
        assertThat(entity.getShipmentDate()).isEqualTo(testBeerOrderShipmentDto.getShipmentDate());
        assertThat(entity.getCarrier()).isEqualTo(testBeerOrderShipmentDto.getCarrier());
        assertThat(entity.getTrackingNumber()).isEqualTo(testBeerOrderShipmentDto.getTrackingNumber());
    }

    @Test
    void updateBeerOrderShipment() {
        // given
        BeerOrderShipmentDto dto = BeerOrderShipmentDto.builder()
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        BeerOrderShipment existingShipment = BeerOrderShipment.builder()
                .id(1)
                .version(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        // when - update with same beer order
        BeerOrderShipment updatedShipment = beerOrderShipmentMapper.updateBeerOrderShipment(dto, existingShipment, null);

        // then
        assertThat(updatedShipment).isNotNull();
        assertThat(updatedShipment.getId()).isEqualTo(existingShipment.getId());
        assertThat(updatedShipment.getVersion()).isEqualTo(existingShipment.getVersion());
        assertThat(updatedShipment.getBeerOrder()).isEqualTo(existingShipment.getBeerOrder());
        assertThat(updatedShipment.getShipmentDate()).isEqualTo(dto.getShipmentDate());
        assertThat(updatedShipment.getCarrier()).isEqualTo(dto.getCarrier());
        assertThat(updatedShipment.getTrackingNumber()).isEqualTo(dto.getTrackingNumber());

        // when - update with new beer order
        BeerOrder newBeerOrder = BeerOrder.builder().id(2).build();
        updatedShipment = beerOrderShipmentMapper.updateBeerOrderShipment(dto, existingShipment, newBeerOrder);

        // then
        assertThat(updatedShipment.getBeerOrder()).isEqualTo(newBeerOrder);
    }
}