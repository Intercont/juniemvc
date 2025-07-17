package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.models.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    /**
     * Get all customers
     * @return List of all customers
     */
    List<CustomerDto> getAllCustomers();

    /**
     * Get a customer by its ID
     * @param id The customer ID
     * @return Optional containing the customer if found
     */
    Optional<CustomerDto> getCustomerById(Integer id);

    /**
     * Save a new customer
     * @param customerDto The customer to save
     * @return The saved customer
     */
    CustomerDto saveCustomer(CustomerDto customerDto);

    /**
     * Update an existing customer
     * @param id The customer ID
     * @param customerDto The updated customer data
     * @return Optional containing the updated customer if found
     */
    Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto);

    /**
     * Delete a customer by its ID
     * @param id The customer ID
     * @return true if the customer was deleted, false if not found
     */
    boolean deleteCustomer(Integer id);
}