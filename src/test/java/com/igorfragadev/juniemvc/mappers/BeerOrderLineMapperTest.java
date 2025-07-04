package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.models.BeerOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BeerOrderLineMapperTest {

    private final BeerMapper beerMapper = Mappers.getMapper(BeerMapper.class);
    private final BeerOrderLineMapper beerOrderLineMapper = Mappers.getMapper(BeerOrderLineMapper.class);

    private BeerOrderLine testBeerOrderLine;
    private BeerOrderLineDto testBeerOrderLineDto;
    private Beer testBeer;
    private BeerOrder testBeerOrder;

    @BeforeEach
    void setUp() {
        // Set up the beerMapper in the BeerOrderLineMapperImpl
        ReflectionTestUtils.setField(beerOrderLineMapper, "beerMapper", beerMapper);

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

        BeerDto testBeerDto = beerMapper.beerToBeerDto(testBeer);

        // Create test beer order
        testBeerOrder = BeerOrder.builder()
                .id(1)
                .version(1)
                .customerRef("TEST-REF-001")
                .paymentAmount(new BigDecimal("129.90"))
                .status("NEW")
                .createdDate(now)
                .updatedDate(now)
                .build();

        // Create test beer order line
        testBeerOrderLine = BeerOrderLine.builder()
                .id(1)
                .version(1)
                .beer(testBeer)
                .beerOrder(testBeerOrder)
                .orderQuantity(10)
                .quantityAllocated(5)
                .status("NEW")
                .createdDate(now)
                .updatedDate(now)
                .build();

        // Create test beer order line DTO
        testBeerOrderLineDto = BeerOrderLineDto.builder()
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
    }

    @Test
    void beerOrderLineToBeerOrderLineDto() {
        // when
        BeerOrderLineDto beerOrderLineDto = beerOrderLineMapper.beerOrderLineToBeerOrderLineDto(testBeerOrderLine);

        // then
        assertThat(beerOrderLineDto).isNotNull();
        assertThat(beerOrderLineDto.getId()).isEqualTo(testBeerOrderLine.getId());
        assertThat(beerOrderLineDto.getVersion()).isEqualTo(testBeerOrderLine.getVersion());
        assertThat(beerOrderLineDto.getBeerId()).isEqualTo(testBeerOrderLine.getBeer().getId());
        assertThat(beerOrderLineDto.getOrderQuantity()).isEqualTo(testBeerOrderLine.getOrderQuantity());
        assertThat(beerOrderLineDto.getQuantityAllocated()).isEqualTo(testBeerOrderLine.getQuantityAllocated());
        assertThat(beerOrderLineDto.getStatus()).isEqualTo(testBeerOrderLine.getStatus());
        assertThat(beerOrderLineDto.getCreatedDate()).isEqualTo(testBeerOrderLine.getCreatedDate());
        assertThat(beerOrderLineDto.getUpdatedDate()).isEqualTo(testBeerOrderLine.getUpdatedDate());

        // Check beer details
        assertThat(beerOrderLineDto.getBeer()).isNotNull();
        assertThat(beerOrderLineDto.getBeer().getId()).isEqualTo(testBeerOrderLine.getBeer().getId());
        assertThat(beerOrderLineDto.getBeer().getBeerName()).isEqualTo(testBeerOrderLine.getBeer().getBeerName());
    }

    @Test
    void beerOrderLineDtoToBeerOrderLine() {
        // when
        BeerOrderLine beerOrderLine = beerOrderLineMapper.beerOrderLineDtoToBeerOrderLine(testBeerOrderLineDto);

        // then
        assertThat(beerOrderLine).isNotNull();
        // id, beer, beerOrder, createdDate, and updatedDate should be ignored
        assertThat(beerOrderLine.getId()).isNull();
        assertThat(beerOrderLine.getBeer()).isNull();
        assertThat(beerOrderLine.getBeerOrder()).isNull();
        assertThat(beerOrderLine.getCreatedDate()).isNull();
        assertThat(beerOrderLine.getUpdatedDate()).isNull();

        // other fields should be mapped
        assertThat(beerOrderLine.getVersion()).isEqualTo(testBeerOrderLineDto.getVersion());
        assertThat(beerOrderLine.getOrderQuantity()).isEqualTo(testBeerOrderLineDto.getOrderQuantity());
        assertThat(beerOrderLine.getQuantityAllocated()).isEqualTo(testBeerOrderLineDto.getQuantityAllocated());
        assertThat(beerOrderLine.getStatus()).isEqualTo(testBeerOrderLineDto.getStatus());
    }
}
