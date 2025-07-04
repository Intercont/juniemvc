package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.models.BeerOrderDto;
import com.igorfragadev.juniemvc.models.BeerOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeerOrderMapperTest {

    private final BeerOrderMapper beerOrderMapper = Mappers.getMapper(BeerOrderMapper.class);
    private final BeerOrderLineMapper beerOrderLineMapper = Mappers.getMapper(BeerOrderLineMapper.class);
    private final BeerMapper beerMapper = Mappers.getMapper(BeerMapper.class);

    private BeerOrder testBeerOrder;
    private BeerOrderDto testBeerOrderDto;
    private Beer testBeer;
    private BeerDto testBeerDto;

    @BeforeEach
    void setUp() {
        // Set up mapper dependencies using reflection
        injectMapperDependencies();

        LocalDateTime now = LocalDateTime.now();

        // Create test beer
        testBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(now)
                .updatedDate(now)
                .build();

        testBeerDto = beerMapper.beerToBeerDto(testBeer);

        // Create test beer order line
        BeerOrderLine testBeerOrderLine = BeerOrderLine.builder()
                .id(1)
                .version(1)
                .beer(testBeer)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .createdDate(now)
                .updatedDate(now)
                .build();

        List<BeerOrderLine> orderLines = new ArrayList<>();
        orderLines.add(testBeerOrderLine);

        // Create test beer order
        testBeerOrder = BeerOrder.builder()
                .id(1)
                .version(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .createdDate(now)
                .updatedDate(now)
                .beerOrderLines(orderLines)
                .build();

        // Set bidirectional relationship
        testBeerOrderLine.setBeerOrder(testBeerOrder);

        // Create test beer order line DTO
        BeerOrderLineDto testBeerOrderLineDto = BeerOrderLineDto.builder()
                .id(1)
                .version(1)
                .beerId(1)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .createdDate(now)
                .updatedDate(now)
                .beer(testBeerDto)
                .build();

        List<BeerOrderLineDto> orderLineDtos = new ArrayList<>();
        orderLineDtos.add(testBeerOrderLineDto);

        // Create test beer order DTO
        testBeerOrderDto = BeerOrderDto.builder()
                .id(1)
                .version(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .createdDate(now)
                .updatedDate(now)
                .beerOrderLines(orderLineDtos)
                .build();
    }

    private void injectMapperDependencies() {
        // Inject BeerOrderLineMapper into BeerOrderMapperImpl
        ReflectionTestUtils.setField(beerOrderMapper, "beerOrderLineMapper", beerOrderLineMapper);

        // Inject BeerMapper into BeerOrderLineMapperImpl
        ReflectionTestUtils.setField(beerOrderLineMapper, "beerMapper", beerMapper);
    }

    @Test
    void beerOrderToBeerOrderDto() {
        // when
        BeerOrderDto beerOrderDto = beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder);

        // then
        assertThat(beerOrderDto).isNotNull();
        assertThat(beerOrderDto.getId()).isEqualTo(testBeerOrder.getId());
        assertThat(beerOrderDto.getVersion()).isEqualTo(testBeerOrder.getVersion());
        assertThat(beerOrderDto.getCustomerRef()).isEqualTo(testBeerOrder.getCustomerRef());
        assertThat(beerOrderDto.getPaymentAmount()).isEqualTo(testBeerOrder.getPaymentAmount());
        assertThat(beerOrderDto.getStatus()).isEqualTo(testBeerOrder.getStatus());
        assertThat(beerOrderDto.getCreatedDate()).isEqualTo(testBeerOrder.getCreatedDate());
        assertThat(beerOrderDto.getUpdatedDate()).isEqualTo(testBeerOrder.getUpdatedDate());

        // Check order lines
        assertThat(beerOrderDto.getBeerOrderLines()).hasSize(1);
        BeerOrderLineDto lineDto = beerOrderDto.getBeerOrderLines().get(0);
        BeerOrderLine line = testBeerOrder.getBeerOrderLines().get(0);
        assertThat(lineDto.getId()).isEqualTo(line.getId());
        assertThat(lineDto.getBeerId()).isEqualTo(line.getBeer().getId());
        assertThat(lineDto.getOrderQuantity()).isEqualTo(line.getOrderQuantity());
    }

    @Test
    void beerOrderDtoToBeerOrder() {
        // when
        BeerOrder beerOrder = beerOrderMapper.beerOrderDtoToBeerOrder(testBeerOrderDto);

        // then
        assertThat(beerOrder).isNotNull();
        // id, createdDate, and updatedDate should be ignored
        assertThat(beerOrder.getId()).isNull();
        assertThat(beerOrder.getCreatedDate()).isNull();
        assertThat(beerOrder.getUpdatedDate()).isNull();

        // other fields should be mapped
        assertThat(beerOrder.getVersion()).isEqualTo(testBeerOrderDto.getVersion());
        assertThat(beerOrder.getCustomerRef()).isEqualTo(testBeerOrderDto.getCustomerRef());
        assertThat(beerOrder.getPaymentAmount()).isEqualTo(testBeerOrderDto.getPaymentAmount());
        assertThat(beerOrder.getStatus()).isEqualTo(testBeerOrderDto.getStatus());

        // Check order lines
        assertThat(beerOrder.getBeerOrderLines()).hasSize(1);
        BeerOrderLine line = beerOrder.getBeerOrderLines().get(0);
        BeerOrderLineDto lineDto = testBeerOrderDto.getBeerOrderLines().get(0);
        assertThat(line.getId()).isNull(); // ID should be ignored
        assertThat(line.getOrderQuantity()).isEqualTo(lineDto.getOrderQuantity());
        assertThat(line.getStatus()).isEqualTo(lineDto.getStatus());
    }
}
