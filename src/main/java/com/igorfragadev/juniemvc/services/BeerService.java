package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.models.BeerDto;

import java.util.List;
import java.util.Optional;

public interface BeerService {

    /**
     * Get all beers
     * @return List of all beers
     */
    List<BeerDto> getAllBeers();

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
}
