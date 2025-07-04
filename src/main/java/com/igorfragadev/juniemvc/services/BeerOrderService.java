package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.models.BeerOrderDto;

import java.util.List;
import java.util.Optional;

public interface BeerOrderService {
    /**
     * Get all beer orders
     * @return List of all beer orders
     */
    List<BeerOrderDto> getAllBeerOrders();

    /**
     * Get a beer order by its ID
     * @param id The beer order ID
     * @return Optional containing the beer order if found
     */
    Optional<BeerOrderDto> getBeerOrderById(Integer id);

    /**
     * Save a new beer order
     * @param beerOrderDto The beer order to save
     * @return The saved beer order
     */
    BeerOrderDto saveBeerOrder(BeerOrderDto beerOrderDto);

    /**
     * Update an existing beer order
     * @param id The beer order ID
     * @param beerOrderDto The updated beer order data
     * @return Optional containing the updated beer order if found
     */
    Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto);

    /**
     * Delete a beer order by its ID
     * @param id The beer order ID
     * @return true if the beer order was deleted, false if not found
     */
    boolean deleteBeerOrder(Integer id);
}