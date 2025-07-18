package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderShipment;
import com.igorfragadev.juniemvc.mappers.BeerOrderShipmentMapper;
import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;
import com.igorfragadev.juniemvc.repositories.BeerOrderRepository;
import com.igorfragadev.juniemvc.repositories.BeerOrderShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeerOrderShipmentServiceImplTest {

    @Mock
    BeerOrderShipmentRepository beerOrderShipmentRepository;

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Mock
    BeerOrderShipmentMapper beerOrderShipmentMapper;

    @InjectMocks
    BeerOrderShipmentServiceImpl beerOrderShipmentService;

    BeerOrder testBeerOrder;
    BeerOrderShipment testBeerOrderShipment;
    BeerOrderShipmentDto testBeerOrderShipmentDto;

    @BeforeEach
    void setUp() {
        // Create test beer order
        testBeerOrder = BeerOrder.builder()
                .id(1)
                .customerRef("TEST-REF-001")
                .status("NEW")
                .build();

        // Create test beer order shipment
        testBeerOrderShipment = BeerOrderShipment.builder()
                .id(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        // Create test beer order shipment DTO
        testBeerOrderShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .beerOrderId(1)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();
    }

    @Test
    void getAllBeerOrderShipments() {
        // given
        given(beerOrderShipmentRepository.findAll()).willReturn(Arrays.asList(testBeerOrderShipment));
        given(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).willReturn(testBeerOrderShipmentDto);

        // when
        List<BeerOrderShipmentDto> shipments = beerOrderShipmentService.getAllBeerOrderShipments();

        // then
        assertThat(shipments).hasSize(1);
        assertThat(shipments.get(0).getCarrier()).isEqualTo("UPS");
        verify(beerOrderShipmentRepository, times(1)).findAll();
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment);
    }

    @Test
    void getShipmentsByBeerOrderId() {
        // given
        given(beerOrderShipmentRepository.findByBeerOrderId(1)).willReturn(Arrays.asList(testBeerOrderShipment));
        given(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).willReturn(testBeerOrderShipmentDto);

        // when
        List<BeerOrderShipmentDto> shipments = beerOrderShipmentService.getShipmentsByBeerOrderId(1);

        // then
        assertThat(shipments).hasSize(1);
        assertThat(shipments.get(0).getBeerOrderId()).isEqualTo(1);
        verify(beerOrderShipmentRepository, times(1)).findByBeerOrderId(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment);
    }

    @Test
    void getBeerOrderShipmentById() {
        // given
        given(beerOrderShipmentRepository.findById(1)).willReturn(Optional.of(testBeerOrderShipment));
        given(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).willReturn(testBeerOrderShipmentDto);

        // when
        Optional<BeerOrderShipmentDto> foundShipment = beerOrderShipmentService.getBeerOrderShipmentById(1);

        // then
        assertThat(foundShipment).isPresent();
        assertThat(foundShipment.get().getId()).isEqualTo(1);
        assertThat(foundShipment.get().getCarrier()).isEqualTo("UPS");
        verify(beerOrderShipmentRepository, times(1)).findById(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment);
    }

    @Test
    void getBeerOrderShipmentByIdNotFound() {
        // given
        given(beerOrderShipmentRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<BeerOrderShipmentDto> foundShipment = beerOrderShipmentService.getBeerOrderShipmentById(999);

        // then
        assertThat(foundShipment).isEmpty();
        verify(beerOrderShipmentRepository, times(1)).findById(999);
    }

    @Test
    void saveBeerOrderShipment() {
        // given
        BeerOrderShipmentDto shipmentDtoToSave = BeerOrderShipmentDto.builder()
                .beerOrderId(1)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        BeerOrderShipment shipmentToSave = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        BeerOrderShipment savedShipment = BeerOrderShipment.builder()
                .id(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        given(beerOrderRepository.findById(1)).willReturn(Optional.of(testBeerOrder));
        given(beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(shipmentDtoToSave)).willReturn(shipmentToSave);
        given(beerOrderShipmentRepository.save(any(BeerOrderShipment.class))).willReturn(savedShipment);
        given(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(savedShipment)).willReturn(testBeerOrderShipmentDto);

        // when
        BeerOrderShipmentDto result = beerOrderShipmentService.saveBeerOrderShipment(shipmentDtoToSave);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getCarrier()).isEqualTo("UPS");
        verify(beerOrderRepository, times(1)).findById(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentDtoToBeerOrderShipment(shipmentDtoToSave);
        verify(beerOrderShipmentRepository, times(1)).save(any(BeerOrderShipment.class));
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(savedShipment);
    }

    @Test
    void saveBeerOrderShipmentBeerOrderNotFound() {
        // given
        BeerOrderShipmentDto shipmentDtoToSave = BeerOrderShipmentDto.builder()
                .beerOrderId(999)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        given(beerOrderRepository.findById(999)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> beerOrderShipmentService.saveBeerOrderShipment(shipmentDtoToSave))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Beer Order not found with ID: 999");
    }

    @Test
    void updateBeerOrderShipment() {
        // given
        BeerOrderShipmentDto shipmentDtoToUpdate = BeerOrderShipmentDto.builder()
                .beerOrderId(1)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        BeerOrderShipment existingShipment = BeerOrderShipment.builder()
                .id(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        BeerOrderShipment updatedShipment = BeerOrderShipment.builder()
                .id(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        BeerOrderShipmentDto updatedShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .beerOrderId(1)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        given(beerOrderShipmentRepository.findById(1)).willReturn(Optional.of(existingShipment));
        given(beerOrderShipmentMapper.updateBeerOrderShipment(any(), any(), any())).willReturn(updatedShipment);
        given(beerOrderShipmentRepository.save(any(BeerOrderShipment.class))).willReturn(updatedShipment);
        given(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(updatedShipment)).willReturn(updatedShipmentDto);

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.updateBeerOrderShipment(1, shipmentDtoToUpdate);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getCarrier()).isEqualTo("FedEx");
        assertThat(result.get().getTrackingNumber()).isEqualTo("9876543210");
        verify(beerOrderShipmentRepository, times(1)).findById(1);
        verify(beerOrderShipmentRepository, times(1)).save(any(BeerOrderShipment.class));
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(updatedShipment);
    }

    @Test
    void updateBeerOrderShipmentWithNewBeerOrder() {
        // given
        BeerOrder newBeerOrder = BeerOrder.builder().id(2).build();
        
        BeerOrderShipmentDto shipmentDtoToUpdate = BeerOrderShipmentDto.builder()
                .beerOrderId(2)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        BeerOrderShipment existingShipment = BeerOrderShipment.builder()
                .id(1)
                .beerOrder(testBeerOrder)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .build();

        BeerOrderShipment updatedShipment = BeerOrderShipment.builder()
                .id(1)
                .beerOrder(newBeerOrder)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        BeerOrderShipmentDto updatedShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .beerOrderId(2)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        given(beerOrderShipmentRepository.findById(1)).willReturn(Optional.of(existingShipment));
        given(beerOrderRepository.findById(2)).willReturn(Optional.of(newBeerOrder));
        given(beerOrderShipmentMapper.updateBeerOrderShipment(any(), any(), any())).willReturn(updatedShipment);
        given(beerOrderShipmentRepository.save(any(BeerOrderShipment.class))).willReturn(updatedShipment);
        given(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(updatedShipment)).willReturn(updatedShipmentDto);

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.updateBeerOrderShipment(1, shipmentDtoToUpdate);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getBeerOrderId()).isEqualTo(2);
        verify(beerOrderShipmentRepository, times(1)).findById(1);
        verify(beerOrderRepository, times(1)).findById(2);
        verify(beerOrderShipmentRepository, times(1)).save(any(BeerOrderShipment.class));
    }

    @Test
    void updateBeerOrderShipmentNotFound() {
        // given
        BeerOrderShipmentDto shipmentDtoToUpdate = BeerOrderShipmentDto.builder()
                .beerOrderId(1)
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("9876543210")
                .build();

        given(beerOrderShipmentRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.updateBeerOrderShipment(999, shipmentDtoToUpdate);

        // then
        assertThat(result).isEmpty();
        verify(beerOrderShipmentRepository, times(1)).findById(999);
        verify(beerOrderShipmentRepository, times(0)).save(any(BeerOrderShipment.class));
    }

    @Test
    void deleteBeerOrderShipment() {
        // given
        given(beerOrderShipmentRepository.existsById(1)).willReturn(true);

        // when
        boolean result = beerOrderShipmentService.deleteBeerOrderShipment(1);

        // then
        assertThat(result).isTrue();
        verify(beerOrderShipmentRepository, times(1)).existsById(1);
        verify(beerOrderShipmentRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteBeerOrderShipmentNotFound() {
        // given
        given(beerOrderShipmentRepository.existsById(999)).willReturn(false);

        // when
        boolean result = beerOrderShipmentService.deleteBeerOrderShipment(999);

        // then
        assertThat(result).isFalse();
        verify(beerOrderShipmentRepository, times(1)).existsById(999);
        verify(beerOrderShipmentRepository, times(0)).deleteById(anyInt());
    }
}