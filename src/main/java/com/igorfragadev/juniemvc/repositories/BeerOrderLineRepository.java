package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // Add custom query methods as needed
}