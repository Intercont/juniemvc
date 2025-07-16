package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Spring Data JPA will automatically implement CRUD operations
    // No additional methods needed for basic CRUD
}