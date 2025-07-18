package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.BeerOrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, Integer> {
    // Find shipments by beer order ID
    List<BeerOrderShipment> findByBeerOrderId(Integer beerOrderId);
}