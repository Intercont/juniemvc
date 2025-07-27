package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.models.BeerPathDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BeerService {

    /**
     * Get all beers
     * @return List of all beers
     */
    List<BeerDto> getAllBeers();

    /**
     * Get all beers with optional filtering by beer name and beer style
     * @param beerName The beer name to filter by (can be null)
     * @param beerStyle The beer style to filter by (can be null)
     * @param pageable The pagination information
     * @return A page of beers
     */
    Page<BeerDto> getAllBeers(String beerName, String beerStyle, Pageable pageable);

    /**
     * Get a beer by its ID
     * @param id The beer ID
     * @return Optional containing the beer if found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Save a new beer
     * @param beerDto The beer to save
     * @return The saved beer
     */
    BeerDto saveBeer(BeerDto beerDto);

    /**
     * Update an existing beer
     * @param id The beer ID
     * @param beerDto The updated beer data
     * @return Optional containing the updated beer if found
     */
    Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto);

    /**
     * Delete a beer by its ID
     * @param id The beer ID
     * @return true if the beer was deleted, false if not found
     */
    boolean deleteBeer(Integer id);

    /**
     * Partially update a beer with the provided data
     * @param id The beer ID
     * @param beerPathDto The partial beer data to update
     * @return Optional containing the updated beer if found
     */
    Optional<BeerDto> patchBeer(Integer id, BeerPathDto beerPathDto);
}
