package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import com.igorfragadev.juniemvc.mappers.BeerOrderMapper;
import com.igorfragadev.juniemvc.models.BeerOrderDto;
import com.igorfragadev.juniemvc.models.BeerOrderLineDto;
import com.igorfragadev.juniemvc.repositories.BeerOrderRepository;
import com.igorfragadev.juniemvc.repositories.BeerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
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
class BeerOrderServiceImplTest {

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerOrderMapper beerOrderMapper;

    @InjectMocks
    BeerOrderServiceImpl beerOrderService;

    BeerOrder testBeerOrder;
    BeerOrderDto testBeerOrderDto;
    Beer testBeer;
    BeerOrderLine testBeerOrderLine;
    BeerOrderLineDto testBeerOrderLineDto;

    @BeforeEach
    void setUp() {
        // Create test beer
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // Create test beer order line
        testBeerOrderLine = BeerOrderLine.builder()
                .id(1)
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        List<BeerOrderLine> orderLines = new ArrayList<>();
        orderLines.add(testBeerOrderLine);

        // Create test beer order
        testBeerOrder = BeerOrder.builder()
                .id(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .beerOrderLines(orderLines)
                .build();

        // Set bidirectional relationship
        testBeerOrderLine.setBeerOrder(testBeerOrder);

        // Create test beer order line DTO
        testBeerOrderLineDto = BeerOrderLineDto.builder()
                .id(1)
                .beerId(1)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .build();

        List<BeerOrderLineDto> orderLineDtos = new ArrayList<>();
        orderLineDtos.add(testBeerOrderLineDto);

        // Create test beer order DTO
        testBeerOrderDto = BeerOrderDto.builder()
                .id(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .beerOrderLines(orderLineDtos)
                .build();
    }

    @Test
    void getAllBeerOrders() {
        // given
        given(beerOrderRepository.findAll()).willReturn(Arrays.asList(testBeerOrder));
        given(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).willReturn(testBeerOrderDto);

        // when
        List<BeerOrderDto> beerOrders = beerOrderService.getAllBeerOrders();

        // then
        assertThat(beerOrders).hasSize(1);
        assertThat(beerOrders.get(0).getCustomerRef()).isEqualTo("TEST-REF-001");
        verify(beerOrderRepository, times(1)).findAll();
        verify(beerOrderMapper, times(1)).beerOrderToBeerOrderDto(testBeerOrder);
    }

    @Test
    void getBeerOrderById() {
        // given
        given(beerOrderRepository.findById(1)).willReturn(Optional.of(testBeerOrder));
        given(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).willReturn(testBeerOrderDto);

        // when
        Optional<BeerOrderDto> foundBeerOrder = beerOrderService.getBeerOrderById(1);

        // then
        assertThat(foundBeerOrder).isPresent();
        assertThat(foundBeerOrder.get().getId()).isEqualTo(1);
        assertThat(foundBeerOrder.get().getCustomerRef()).isEqualTo("TEST-REF-001");
        verify(beerOrderRepository, times(1)).findById(1);
        verify(beerOrderMapper, times(1)).beerOrderToBeerOrderDto(testBeerOrder);
    }

    @Test
    void getBeerOrderByIdNotFound() {
        // given
        given(beerOrderRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<BeerOrderDto> foundBeerOrder = beerOrderService.getBeerOrderById(999);

        // then
        assertThat(foundBeerOrder).isEmpty();
        verify(beerOrderRepository, times(1)).findById(999);
    }

    @Test
    void saveBeerOrder() {
        // given
        BeerOrderDto beerOrderDtoToSave = BeerOrderDto.builder()
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .beerOrderLines(Arrays.asList(testBeerOrderLineDto))
                .build();

        BeerOrder beerOrderToSave = BeerOrder.builder()
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .build();

        BeerOrder savedBeerOrder = BeerOrder.builder()
                .id(2)
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .build();

        BeerOrderDto savedBeerOrderDto = BeerOrderDto.builder()
                .id(2)
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .build();

        given(beerOrderMapper.beerOrderDtoToBeerOrder(beerOrderDtoToSave)).willReturn(beerOrderToSave);
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));
        given(beerOrderRepository.save(any(BeerOrder.class))).willReturn(savedBeerOrder);
        given(beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder)).willReturn(savedBeerOrderDto);

        // when
        BeerOrderDto result = beerOrderService.saveBeerOrder(beerOrderDtoToSave);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getCustomerRef()).isEqualTo("NEW-REF-001");
        verify(beerOrderMapper, times(1)).beerOrderDtoToBeerOrder(beerOrderDtoToSave);
        verify(beerRepository, times(1)).findById(1);
        verify(beerOrderRepository, times(1)).save(any(BeerOrder.class));
        verify(beerOrderMapper, times(1)).beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Test
    void saveBeerOrderBeerNotFound() {
        // given
        BeerOrderDto beerOrderDtoToSave = BeerOrderDto.builder()
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .beerOrderLines(Arrays.asList(testBeerOrderLineDto))
                .build();

        BeerOrder beerOrderToSave = BeerOrder.builder()
                .customerRef("NEW-REF-001")
                .paymentAmount(new BigDecimal("99.90"))
                .status("NEW")
                .build();

        given(beerOrderMapper.beerOrderDtoToBeerOrder(beerOrderDtoToSave)).willReturn(beerOrderToSave);
        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> beerOrderService.saveBeerOrder(beerOrderDtoToSave))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Beer not found with ID: 1");
    }

    @Test
    void updateBeerOrder() {
        // given
        BeerOrderDto beerOrderDtoToUpdate = BeerOrderDto.builder()
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        BeerOrder existingBeerOrder = BeerOrder.builder()
                .id(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .build();

        BeerOrder updatedBeerOrder = BeerOrder.builder()
                .id(1)
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        BeerOrderDto updatedBeerOrderDto = BeerOrderDto.builder()
                .id(1)
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        given(beerOrderRepository.findById(1)).willReturn(Optional.of(existingBeerOrder));
        given(beerOrderRepository.save(any(BeerOrder.class))).willReturn(updatedBeerOrder);
        given(beerOrderMapper.beerOrderToBeerOrderDto(updatedBeerOrder)).willReturn(updatedBeerOrderDto);

        // when
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrder(1, beerOrderDtoToUpdate);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getCustomerRef()).isEqualTo("UPDATED-REF-001");
        assertThat(result.get().getPaymentAmount()).isEqualTo(new BigDecimal("149.90"));
        assertThat(result.get().getStatus()).isEqualTo("UPDATED");
        verify(beerOrderRepository, times(1)).findById(1);
        verify(beerOrderRepository, times(1)).save(any(BeerOrder.class));
        verify(beerOrderMapper, times(1)).beerOrderToBeerOrderDto(updatedBeerOrder);
    }

    @Test
    void updateBeerOrderNotFound() {
        // given
        BeerOrderDto beerOrderDtoToUpdate = BeerOrderDto.builder()
                .customerRef("UPDATED-REF-001")
                .paymentAmount(new BigDecimal("149.90"))
                .status("UPDATED")
                .build();

        given(beerOrderRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrder(999, beerOrderDtoToUpdate);

        // then
        assertThat(result).isEmpty();
        verify(beerOrderRepository, times(1)).findById(999);
        verify(beerOrderRepository, times(0)).save(any(BeerOrder.class));
    }

    @Test
    void deleteBeerOrder() {
        // given
        given(beerOrderRepository.existsById(1)).willReturn(true);

        // when
        boolean result = beerOrderService.deleteBeerOrder(1);

        // then
        assertThat(result).isTrue();
        verify(beerOrderRepository, times(1)).existsById(1);
        verify(beerOrderRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteBeerOrderNotFound() {
        // given
        given(beerOrderRepository.existsById(999)).willReturn(false);

        // when
        boolean result = beerOrderService.deleteBeerOrder(999);

        // then
        assertThat(result).isFalse();
        verify(beerOrderRepository, times(1)).existsById(999);
        verify(beerOrderRepository, times(0)).deleteById(anyInt());
    }
}