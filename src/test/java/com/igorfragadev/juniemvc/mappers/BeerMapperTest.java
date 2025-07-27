package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.models.BeerPathDto;
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
                .imageUrl("http://example.com/image.jpg")
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
        assertThat(beerDto.getImageUrl()).isEqualTo(beer.getImageUrl());
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
                .imageUrl("http://example.com/image.jpg")
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
        assertThat(beer.getImageUrl()).isEqualTo(beerDto.getImageUrl());
    }

    @Test
    void updateBeerFromBeerPathDto() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Beer existingBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Original Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .imageUrl("http://example.com/image.jpg")
                .createdDate(now)
                .updatedDate(now)
                .build();

        // Create a BeerPathDto with only some fields set (partial update)
        BeerPathDto beerPathDto = BeerPathDto.builder()
                .beerName("Updated Beer")
                .price(new BigDecimal("14.99"))
                .build();

        // when
        Beer updatedBeer = beerMapper.updateBeerFromBeerPathDto(beerPathDto, existingBeer);

        // then
        assertThat(updatedBeer).isNotNull();
        // Fields that should be updated
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Beer");
        assertThat(updatedBeer.getPrice()).isEqualTo(new BigDecimal("14.99"));

        // Fields that should remain unchanged
        assertThat(updatedBeer.getId()).isEqualTo(1);
        assertThat(updatedBeer.getVersion()).isEqualTo(1);
        assertThat(updatedBeer.getBeerStyle()).isEqualTo("IPA");
        assertThat(updatedBeer.getUpc()).isEqualTo("123456");
        assertThat(updatedBeer.getQuantityOnHand()).isEqualTo(100);
        assertThat(updatedBeer.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(updatedBeer.getCreatedDate()).isEqualTo(now);
        assertThat(updatedBeer.getUpdatedDate()).isEqualTo(now);
    }

    @Test
    void updateBeerFromBeerPathDtoWithNullValues() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Beer existingBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Original Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .imageUrl("http://example.com/image.jpg")
                .createdDate(now)
                .updatedDate(now)
                .build();

        // Create a BeerPathDto with some fields set to null (should be ignored)
        BeerPathDto beerPathDto = BeerPathDto.builder()
                .beerName("Updated Beer")
                .beerStyle(null) // Should be ignored
                .price(new BigDecimal("14.99"))
                .quantityOnHand(null) // Should be ignored
                .build();

        // when
        Beer updatedBeer = beerMapper.updateBeerFromBeerPathDto(beerPathDto, existingBeer);

        // then
        assertThat(updatedBeer).isNotNull();
        // Fields that should be updated
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Beer");
        assertThat(updatedBeer.getPrice()).isEqualTo(new BigDecimal("14.99"));

        // Fields that should remain unchanged because they were null in the DTO
        assertThat(updatedBeer.getBeerStyle()).isEqualTo("IPA");
        assertThat(updatedBeer.getQuantityOnHand()).isEqualTo(100);

        // Other fields that should remain unchanged
        assertThat(updatedBeer.getId()).isEqualTo(1);
        assertThat(updatedBeer.getVersion()).isEqualTo(1);
        assertThat(updatedBeer.getUpc()).isEqualTo("123456");
        assertThat(updatedBeer.getImageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(updatedBeer.getCreatedDate()).isEqualTo(now);
        assertThat(updatedBeer.getUpdatedDate()).isEqualTo(now);
    }
}
