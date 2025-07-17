package com.igorfragadev.juniemvc.repositories;

import com.igorfragadev.juniemvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        // When
        Customer savedCustomer = customerRepository.save(customer);

        // Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }

    @Test
    void testGetCustomerById() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        // When
        Optional<Customer> fetchedCustomerOptional = customerRepository.findById(savedCustomer.getId());

        // Then
        assertThat(fetchedCustomerOptional).isPresent();
        Customer fetchedCustomer = fetchedCustomerOptional.get();
        assertThat(fetchedCustomer.getName()).isEqualTo("John Doe");
        assertThat(fetchedCustomer.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testUpdateCustomer() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        // When
        savedCustomer.setName("Jane Doe");
        savedCustomer.setEmail("jane.doe@example.com");
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        // Then
        assertThat(updatedCustomer.getName()).isEqualTo("Jane Doe");
        assertThat(updatedCustomer.getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void testDeleteCustomer() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        // When
        customerRepository.delete(savedCustomer);
        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());

        // Then
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    void testListCustomers() {
        // Given
        Customer customer1 = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();
        
        Customer customer2 = Customer.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();
        
        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // When
        List<Customer> customers = customerRepository.findAll();

        // Then
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isGreaterThanOrEqualTo(2);
    }
}