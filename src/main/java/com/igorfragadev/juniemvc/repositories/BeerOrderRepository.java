package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // Add custom query methods as needed
}