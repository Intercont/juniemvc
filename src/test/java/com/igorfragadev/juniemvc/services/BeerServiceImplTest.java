package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.mappers.BeerMapper;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BeerServiceImpl beerService;

    Beer testBeer;
    BeerDto testBeerDto;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        testBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void getAllBeers() {
        // given
        given(beerRepository.findAll()).willReturn(Arrays.asList(testBeer));
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // when
        List<BeerDto> beers = beerService.getAllBeers();

        // then
        assertThat(beers).hasSize(1);
        assertThat(beers.get(0).getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository, times(1)).findAll();
        verify(beerMapper, times(1)).beerToBeerDto(testBeer);
    }

    @Test
    void getBeerById() {
        // given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // when
        Optional<BeerDto> foundBeer = beerService.getBeerById(1);

        // then
        assertThat(foundBeer).isPresent();
        assertThat(foundBeer.get().getId()).isEqualTo(1);
        assertThat(foundBeer.get().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository, times(1)).findById(1);
        verify(beerMapper, times(1)).beerToBeerDto(testBeer);
    }

    @Test
    void getBeerByIdNotFound() {
        // given
        given(beerRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<BeerDto> foundBeer = beerService.getBeerById(999);

        // then
        assertThat(foundBeer).isEmpty();
        verify(beerRepository, times(1)).findById(999);
    }

    @Test
    void saveBeer() {
        // given
        BeerDto beerDtoToSave = BeerDto.builder()
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();

        Beer beerToSave = Beer.builder()
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();

        Beer savedBeer = Beer.builder()
                .id(2)
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();

        BeerDto savedBeerDto = BeerDto.builder()
                .id(2)
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();

        given(beerMapper.beerDtoToBeer(beerDtoToSave)).willReturn(beerToSave);
        given(beerRepository.save(any(Beer.class))).willReturn(savedBeer);
        given(beerMapper.beerToBeerDto(savedBeer)).willReturn(savedBeerDto);

        // when
        BeerDto result = beerService.saveBeer(beerDtoToSave);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getBeerName()).isEqualTo("New Beer");
        verify(beerMapper, times(1)).beerDtoToBeer(beerDtoToSave);
        verify(beerRepository, times(1)).save(any(Beer.class));
        verify(beerMapper, times(1)).beerToBeerDto(savedBeer);
    }

    @Test
    void updateBeer() {
        // given
        BeerDto beerDtoToUpdate = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        Beer beerToUpdate = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        Beer existingBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer updatedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        BeerDto updatedBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        given(beerMapper.beerDtoToBeer(beerDtoToUpdate)).willReturn(beerToUpdate);
        given(beerRepository.findById(1)).willReturn(Optional.of(existingBeer));
        given(beerRepository.save(any(Beer.class))).willReturn(updatedBeer);
        given(beerMapper.beerToBeerDto(updatedBeer)).willReturn(updatedBeerDto);

        // when
        Optional<BeerDto> result = beerService.updateBeer(1, beerDtoToUpdate);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getBeerName()).isEqualTo("Updated Beer");
        assertThat(result.get().getBeerStyle()).isEqualTo("Stout");
        verify(beerMapper, times(1)).beerDtoToBeer(beerDtoToUpdate);
        verify(beerRepository, times(1)).findById(1);
        verify(beerRepository, times(1)).save(any(Beer.class));
        verify(beerMapper, times(1)).beerToBeerDto(updatedBeer);
    }

    @Test
    void updateBeerNotFound() {
        // given
        BeerDto beerDtoToUpdate = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("789012")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();

        given(beerRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<BeerDto> result = beerService.updateBeer(999, beerDtoToUpdate);

        // then
        assertThat(result).isEmpty();
        verify(beerRepository, times(1)).findById(999);
        verify(beerRepository, times(0)).save(any(Beer.class));
    }

    @Test
    void deleteBeer() {
        // given
        given(beerRepository.existsById(1)).willReturn(true);

        // when
        boolean result = beerService.deleteBeer(1);

        // then
        assertThat(result).isTrue();
        verify(beerRepository, times(1)).existsById(1);
        verify(beerRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteBeerNotFound() {
        // given
        given(beerRepository.existsById(999)).willReturn(false);

        // when
        boolean result = beerService.deleteBeer(999);

        // then
        assertThat(result).isFalse();
        verify(beerRepository, times(1)).existsById(999);
        verify(beerRepository, times(0)).deleteById(anyInt());
    }
}
