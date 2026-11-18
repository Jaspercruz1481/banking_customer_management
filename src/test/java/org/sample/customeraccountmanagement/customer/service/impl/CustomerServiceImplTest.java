package org.sample.customeraccountmanagement.customer.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sample.customeraccountmanagement.account.entity.Account;
import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import org.sample.customeraccountmanagement.common.exception.CustomerNotFoundException;
import org.sample.customeraccountmanagement.customer.dto.CustomerAccountResponseDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerRequestDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerResponseDto;
import org.sample.customeraccountmanagement.customer.entity.Customer;
import org.sample.customeraccountmanagement.customer.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;


    @Test
    void createCustomer() {
        Customer savedCustomer = Customer.builder()
                .id(1L)
                .customerName("John Doe")
                .customerMobile("1234567890")
                .customerEmail("john.doe@example.com")
                .address1("123 Main St")
                .address2("Apt 4B")
                .build();

        CustomerRequestDto requestDto = CustomerRequestDto.builder()
                .customerName("John Doe")
                .customerMobile("1234567890")
                .customerEmail("john.doe@example.com")
                .address1("123 Main St")
                .address2("Apt 4B")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponseDto responseDto = customerService.createCustomer(requestDto);

        assertEquals(1L, responseDto.getCustomerNumber());
        assertEquals(201, responseDto.getTransactionStatusCode());
        assertEquals("Customer account created", responseDto.getTransactionDescriptionMessage());

        verify(customerRepository).save(any(Customer.class));

    }

    @Test
    void getCustomerByCustomerNumber() {

        Customer customer = Customer.builder()
                .id(1L)
                .customerName("John Doe")
                .customerMobile("1234567890")
                .customerEmail("john.doe@example.com")
                .address1("123 Main St")
                .address2("Apt 4B")
                .accounts(Collections.emptyList())
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerAccountResponseDto responseDto = customerService.getCustomerByCustomerNumber(1L);

        assertEquals(1L, responseDto.getCustomerNumber());
        assertEquals("John Doe", responseDto.getCustomerName());
        assertEquals("1234567890", responseDto.getCustomerMobile());
        assertEquals("john.doe@example.com", responseDto.getCustomerEmail());
        assertEquals("123 Main St", responseDto.getAddress1());
        assertEquals("Apt 4B", responseDto.getAddress2());

        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerByCustomerNumberSeparatesSavingsAndCheckingAccounts() {
        Customer customer = Customer.builder()
                .id(1L)
                .customerName("John Doe")
                .accounts(Arrays.asList(
                        Account.builder()
                                .accountNumber(10001L)
                                .accountType(AccountType.SAVINGS)
                                .balance(new BigDecimal("500.00"))
                                .customer(null)
                                .build(),
                        Account.builder()
                                .accountNumber(10002L)
                                .accountType(AccountType.CHECKING)
                                .balance(new BigDecimal("250.00"))
                                .customer(null)
                                .build()
                ))
                .build();

        customer.getAccounts().forEach(account -> account.setCustomer(customer));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerAccountResponseDto response =
                customerService.getCustomerByCustomerNumber(1L);

        assertEquals(1, response.getSavings().size());
        assertEquals(10001L, response.getSavings().get(0).getAccountNumber());
        assertEquals(1L, response.getSavings().get(0).getCustomerId());

        assertEquals(1, response.getCurrent().size());
        assertEquals(10002L, response.getCurrent().get(0).getAccountNumber());
        assertEquals(1L, response.getCurrent().get(0).getCustomerId());
        assertEquals(302, response.getTransactionStatusCode());

        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerByCustomerNumberThrowsWhenCustomerIsMissing() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomerByCustomerNumber(999L)
        );

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository).findById(999L);
    }
}