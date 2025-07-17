package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Customer;
import com.igorfragadev.juniemvc.models.CustomerDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void customerToCustomerDto() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Customer customer = Customer.builder()
                .id(1)
                .version(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        CustomerDto customerDto = customerMapper.customerToCustomerDto(customer);

        // then
        assertThat(customerDto).isNotNull();
        assertThat(customerDto.getId()).isEqualTo(customer.getId());
        assertThat(customerDto.getVersion()).isEqualTo(customer.getVersion());
        assertThat(customerDto.getName()).isEqualTo(customer.getName());
        assertThat(customerDto.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerDto.getPhone()).isEqualTo(customer.getPhone());
        assertThat(customerDto.getAddressLine1()).isEqualTo(customer.getAddressLine1());
        assertThat(customerDto.getAddressLine2()).isEqualTo(customer.getAddressLine2());
        assertThat(customerDto.getCity()).isEqualTo(customer.getCity());
        assertThat(customerDto.getState()).isEqualTo(customer.getState());
        assertThat(customerDto.getPostalCode()).isEqualTo(customer.getPostalCode());
        assertThat(customerDto.getCreatedAt()).isEqualTo(customer.getCreatedAt());
        assertThat(customerDto.getUpdatedAt()).isEqualTo(customer.getUpdatedAt());
    }

    @Test
    void customerDtoToCustomer() {
        // given
        LocalDateTime now = LocalDateTime.now();
        CustomerDto customerDto = CustomerDto.builder()
                .id(1)
                .version(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // when
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);

        // then
        assertThat(customer).isNotNull();
        // id, createdAt, and updatedAt should be ignored
        assertThat(customer.getId()).isNull();
        assertThat(customer.getCreatedAt()).isNull();
        assertThat(customer.getUpdatedAt()).isNull();
        // other fields should be mapped
        assertThat(customer.getVersion()).isEqualTo(customerDto.getVersion());
        assertThat(customer.getName()).isEqualTo(customerDto.getName());
        assertThat(customer.getEmail()).isEqualTo(customerDto.getEmail());
        assertThat(customer.getPhone()).isEqualTo(customerDto.getPhone());
        assertThat(customer.getAddressLine1()).isEqualTo(customerDto.getAddressLine1());
        assertThat(customer.getAddressLine2()).isEqualTo(customerDto.getAddressLine2());
        assertThat(customer.getCity()).isEqualTo(customerDto.getCity());
        assertThat(customer.getState()).isEqualTo(customerDto.getState());
        assertThat(customer.getPostalCode()).isEqualTo(customerDto.getPostalCode());
    }
}