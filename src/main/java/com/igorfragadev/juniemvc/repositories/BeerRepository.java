package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // No additional methods needed for basic CRUD

    /**
     * Find all beers with optional filtering by beer name
     * @param beerName The beer name to filter by (can be null)
     * @param pageable The pagination information
     * @return A page of beers
     */
    Page<Beer> findAllByBeerNameContainingIgnoreCase(String beerName, Pageable pageable);

    /**
     * Find all beers with optional filtering by beer style
     * @param beerStyle The beer style to filter by (can be null)
     * @param pageable The pagination information
     * @return A page of beers
     */
    Page<Beer> findAllByBeerStyleContainingIgnoreCase(String beerStyle, Pageable pageable);

    /**
     * Find all beers with optional filtering by beer name and beer style
     * @param beerName The beer name to filter by (can be null)
     * @param beerStyle The beer style to filter by (can be null)
     * @param pageable The pagination information
     * @return A page of beers
     */
    Page<Beer> findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
            String beerName, String beerStyle, Pageable pageable);

    /**
     * Find all beers with pagination
     * @param pageable The pagination information
     * @return A page of beers
     */
    Page<Beer> findAll(Pageable pageable);
}
