package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.models.BeerDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BeerMapperTest {

    private final BeerMapper beerMapper = Mappers.getMapper(BeerMapper.class);

    @Test
    void beerToBeerDto() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Beer beer = Beer.builder()
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

        // when
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);

        // then
        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId()).isEqualTo(beer.getId());
        assertThat(beerDto.getVersion()).isEqualTo(beer.getVersion());
        assertThat(beerDto.getBeerName()).isEqualTo(beer.getBeerName());
        assertThat(beerDto.getBeerStyle()).isEqualTo(beer.getBeerStyle());
        assertThat(beerDto.getUpc()).isEqualTo(beer.getUpc());
        assertThat(beerDto.getPrice()).isEqualTo(beer.getPrice());
        assertThat(beerDto.getQuantityOnHand()).isEqualTo(beer.getQuantityOnHand());
        assertThat(beerDto.getCreatedDate()).isEqualTo(beer.getCreatedDate());
        assertThat(beerDto.getUpdatedDate()).isEqualTo(beer.getUpdatedDate());
    }

    @Test
    void beerDtoToBeer() {
        // given
        LocalDateTime now = LocalDateTime.now();
        BeerDto beerDto = BeerDto.builder()
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

        // when
        Beer beer = beerMapper.beerDtoToBeer(beerDto);

        // then
        assertThat(beer).isNotNull();
        // id, createdDate, and updatedDate should be ignored
        assertThat(beer.getId()).isNull();
        assertThat(beer.getCreatedDate()).isNull();
        assertThat(beer.getUpdatedDate()).isNull();
        // other fields should be mapped
        assertThat(beer.getVersion()).isEqualTo(beerDto.getVersion());
        assertThat(beer.getBeerName()).isEqualTo(beerDto.getBeerName());
        assertThat(beer.getBeerStyle()).isEqualTo(beerDto.getBeerStyle());
        assertThat(beer.getUpc()).isEqualTo(beerDto.getUpc());
        assertThat(beer.getPrice()).isEqualTo(beerDto.getPrice());
        assertThat(beer.getQuantityOnHand()).isEqualTo(beerDto.getQuantityOnHand());
    }
}