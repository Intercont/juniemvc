package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.Customer;
import com.igorfragadev.juniemvc.mappers.CustomerMapper;
import com.igorfragadev.juniemvc.models.CustomerDto;
import com.igorfragadev.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerServiceImpl customerService;

    Customer testCustomer;
    CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();
    }

    @Test
    void getAllCustomers() {
        // given
        given(customerRepository.findAll()).willReturn(Arrays.asList(testCustomer));
        given(customerMapper.customerToCustomerDto(testCustomer)).willReturn(testCustomerDto);

        // when
        List<CustomerDto> customers = customerService.getAllCustomers();

        // then
        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getName()).isEqualTo("John Doe");
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).customerToCustomerDto(testCustomer);
    }

    @Test
    void getCustomerById() {
        // given
        given(customerRepository.findById(1)).willReturn(Optional.of(testCustomer));
        given(customerMapper.customerToCustomerDto(testCustomer)).willReturn(testCustomerDto);

        // when
        Optional<CustomerDto> foundCustomer = customerService.getCustomerById(1);

        // then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getId()).isEqualTo(1);
        assertThat(foundCustomer.get().getName()).isEqualTo("John Doe");
        verify(customerRepository, times(1)).findById(1);
        verify(customerMapper, times(1)).customerToCustomerDto(testCustomer);
    }

    @Test
    void getCustomerByIdNotFound() {
        // given
        given(customerRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<CustomerDto> foundCustomer = customerService.getCustomerById(999);

        // then
        assertThat(foundCustomer).isEmpty();
        verify(customerRepository, times(1)).findById(999);
    }

    @Test
    void saveCustomer() {
        // given
        CustomerDto customerDtoToSave = CustomerDto.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();

        Customer customerToSave = Customer.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(2)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();

        CustomerDto savedCustomerDto = CustomerDto.builder()
                .id(2)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .phone("+1-555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Somewhere")
                .state("NY")
                .postalCode("67890")
                .build();

        given(customerMapper.customerDtoToCustomer(customerDtoToSave)).willReturn(customerToSave);
        given(customerRepository.save(any(Customer.class))).willReturn(savedCustomer);
        given(customerMapper.customerToCustomerDto(savedCustomer)).willReturn(savedCustomerDto);

        // when
        CustomerDto result = customerService.saveCustomer(customerDtoToSave);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getName()).isEqualTo("Jane Smith");
        verify(customerMapper, times(1)).customerDtoToCustomer(customerDtoToSave);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).customerToCustomerDto(savedCustomer);
    }

    @Test
    void updateCustomer() {
        // given
        CustomerDto customerDtoToUpdate = CustomerDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer customerToUpdate = Customer.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer existingCustomer = Customer.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer updatedCustomer = Customer.builder()
                .id(1)
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .id(1)
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        given(customerMapper.customerDtoToCustomer(customerDtoToUpdate)).willReturn(customerToUpdate);
        given(customerRepository.findById(1)).willReturn(Optional.of(existingCustomer));
        given(customerRepository.save(any(Customer.class))).willReturn(updatedCustomer);
        given(customerMapper.customerToCustomerDto(updatedCustomer)).willReturn(updatedCustomerDto);

        // when
        Optional<CustomerDto> result = customerService.updateCustomer(1, customerDtoToUpdate);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getName()).isEqualTo("John Updated");
        assertThat(result.get().getEmail()).isEqualTo("john.updated@example.com");
        verify(customerMapper, times(1)).customerDtoToCustomer(customerDtoToUpdate);
        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).customerToCustomerDto(updatedCustomer);
    }

    @Test
    void updateCustomerNotFound() {
        // given
        CustomerDto customerDtoToUpdate = CustomerDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phone("+1-555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 5C")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        given(customerRepository.findById(999)).willReturn(Optional.empty());

        // when
        Optional<CustomerDto> result = customerService.updateCustomer(999, customerDtoToUpdate);

        // then
        assertThat(result).isEmpty();
        verify(customerRepository, times(1)).findById(999);
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    void deleteCustomer() {
        // given
        given(customerRepository.existsById(1)).willReturn(true);

        // when
        boolean result = customerService.deleteCustomer(1);

        // then
        assertThat(result).isTrue();
        verify(customerRepository, times(1)).existsById(1);
        verify(customerRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteCustomerNotFound() {
        // given
        given(customerRepository.existsById(999)).willReturn(false);

        // when
        boolean result = customerService.deleteCustomer(999);

        // then
        assertThat(result).isFalse();
        verify(customerRepository, times(1)).existsById(999);
        verify(customerRepository, times(0)).deleteById(anyInt());
    }
}