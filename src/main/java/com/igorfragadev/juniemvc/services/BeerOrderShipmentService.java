package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;

import java.util.List;
import java.util.Optional;

public interface BeerOrderShipmentService {
    /**
     * Get all beer order shipments
     * @return List of all beer order shipments
     */
    List<BeerOrderShipmentDto> getAllBeerOrderShipments();

    /**
     * Get all shipments for a specific beer order
     * @param beerOrderId The beer order ID
     * @return List of shipments for the beer order
     */
    List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId);

    /**
     * Get a beer order shipment by its ID
     * @param id The beer order shipment ID
     * @return Optional containing the beer order shipment if found
     */
    Optional<BeerOrderShipmentDto> getBeerOrderShipmentById(Integer id);

    /**
     * Save a new beer order shipment
     * @param beerOrderShipmentDto The beer order shipment to save
     * @return The saved beer order shipment
     */
    BeerOrderShipmentDto saveBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);

    /**
     * Update an existing beer order shipment
     * @param id The beer order shipment ID
     * @param beerOrderShipmentDto The updated beer order shipment data
     * @return Optional containing the updated beer order shipment if found
     */
    Optional<BeerOrderShipmentDto> updateBeerOrderShipment(Integer id, BeerOrderShipmentDto beerOrderShipmentDto);

    /**
     * Delete a beer order shipment by its ID
     * @param id The beer order shipment ID
     * @return true if the beer order shipment was deleted, false if not found
     */
    boolean deleteBeerOrderShipment(Integer id);
}