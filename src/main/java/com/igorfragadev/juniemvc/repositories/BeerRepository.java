package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // No additional methods needed for basic CRUD
}