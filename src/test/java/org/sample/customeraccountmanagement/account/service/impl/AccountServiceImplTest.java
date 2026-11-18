package org.sample.customeraccountmanagement.account.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sample.customeraccountmanagement.account.dto.AccountRequestDto;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.account.entity.Account;
import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import org.sample.customeraccountmanagement.account.repository.AccountRepository;
import org.sample.customeraccountmanagement.common.exception.CustomerNotFoundException;
import org.sample.customeraccountmanagement.customer.entity.Customer;
import org.sample.customeraccountmanagement.customer.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount() {
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        AccountRequestDto request = AccountRequestDto.builder()
                .customerId(1L)
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("500.00"))
                .build();

        Account savedAccount = Account.builder()
                .id(10L)
                .accountNumber(1000L)
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("500.00"))
                .customer(customer)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.nextAccountNumber()).thenReturn(1000L);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        AccountResponseDto response = accountService.createAccount(request);

        assertEquals(1L, response.getCustomerId());
        assertEquals(1000L, response.getAccountNumber());
        assertEquals(AccountType.SAVINGS, response.getAccountType());
        assertEquals(new BigDecimal("500.00"), response.getBalance());

        verify(customerRepository).findById(1L);
        verify(accountRepository).nextAccountNumber();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccountThrowsWhenCustomerIsMissing() {
        AccountRequestDto request = AccountRequestDto.builder()
                .customerId(999L)
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("500.00"))
                .build();

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> accountService.createAccount(request)
        );

        assertEquals("Customer not found with ID: 999", exception.getMessage());
        verify(accountRepository, never()).nextAccountNumber();
        verify(accountRepository, never()).save(any(Account.class));
    }
}