package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // When
        Beer savedBeer = beerRepository.save(beer);

        // Then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testGetBeerById() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        Optional<Beer> fetchedBeerOptional = beerRepository.findById(savedBeer.getId());

        // Then
        assertThat(fetchedBeerOptional).isPresent();
        Beer fetchedBeer = fetchedBeerOptional.get();
        assertThat(fetchedBeer.getBeerName()).isEqualTo("Test Beer");
    }

    @Test
    void testUpdateBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        savedBeer.setBeerName("Updated Beer Name");
        Beer updatedBeer = beerRepository.save(savedBeer);

        // Then
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Beer Name");
    }

    @Test
    void testDeleteBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        beerRepository.delete(savedBeer);
        Optional<Beer> deletedBeer = beerRepository.findById(savedBeer.getId());

        // Then
        assertThat(deletedBeer).isEmpty();
    }

    @Test
    void testListBeers() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Test Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        // When
        List<Beer> beers = beerRepository.findAll();

        // Then
        assertThat(beers).isNotNull();
        assertThat(beers.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testFindAllWithPagination() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Test Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAll(pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent().size()).isGreaterThanOrEqualTo(2);
        assertThat(beerPage.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testFindAllByBeerNameContainingIgnoreCase() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Another Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAllByBeerNameContainingIgnoreCase("test", pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent().size()).isEqualTo(1);
        assertThat(beerPage.getContent().get(0).getBeerName()).isEqualTo("Test Beer 1");
    }

    @Test
    void testFindAllByBeerNameContainingIgnoreCaseNoMatch() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Another Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAllByBeerNameContainingIgnoreCase("nonexistent", pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent()).isEmpty();
        assertThat(beerPage.getTotalElements()).isZero();
    }

    @Test
    void testFindAllByBeerStyleContainingIgnoreCase() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Another Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAllByBeerStyleContainingIgnoreCase("ipa", pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent().size()).isEqualTo(1);
        assertThat(beerPage.getContent().get(0).getBeerStyle()).isEqualTo("IPA");
    }

    @Test
    void testFindAllByBeerStyleContainingIgnoreCaseNoMatch() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Another Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAllByBeerStyleContainingIgnoreCase("nonexistent", pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent()).isEmpty();
        assertThat(beerPage.getTotalElements()).isZero();
    }

    @Test
    void testFindAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Test Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Another Beer")
                .beerStyle("IPA")
                .upc("789012")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(150)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                "test", "ipa", pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent().size()).isEqualTo(1);
        assertThat(beerPage.getContent().get(0).getBeerName()).isEqualTo("Test Beer 1");
        assertThat(beerPage.getContent().get(0).getBeerStyle()).isEqualTo("IPA");
    }

    @Test
    void testFindAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCaseNoMatch() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Test Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Another Beer")
                .beerStyle("IPA")
                .upc("789012")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(150)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Beer> beerPage = beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                "nonexistent", "nonexistent", pageable);

        // Then
        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent()).isEmpty();
        assertThat(beerPage.getTotalElements()).isZero();
    }
}
