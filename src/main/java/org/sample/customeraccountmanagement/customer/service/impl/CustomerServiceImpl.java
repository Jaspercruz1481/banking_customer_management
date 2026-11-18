package org.sample.customeraccountmanagement.customer.service.impl;

import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.account.entity.Account;
import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import org.sample.customeraccountmanagement.common.exception.CustomerNotFoundException;
import org.sample.customeraccountmanagement.customer.dto.CustomerAccountResponseDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerRequestDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerResponseDto;
import org.sample.customeraccountmanagement.customer.entity.Customer;
import org.sample.customeraccountmanagement.customer.repository.CustomerRepository;
import org.sample.customeraccountmanagement.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {

        Customer customer = Customer.builder()
                .customerMobile(customerRequestDto.getCustomerMobile())
                .customerName(customerRequestDto.getCustomerName())
                .customerEmail(customerRequestDto.getCustomerEmail())
                .address1(customerRequestDto.getAddress1())
                .address2(customerRequestDto.getAddress2())
                .build();

        Customer saved = customerRepository.save(customer);

        if (saved.getId() != null) {
            return CustomerResponseDto.builder()
                    .customerNumber(saved.getId())
                    .transactionStatusCode(HttpStatus.CREATED.value())
                    .transactionDescriptionMessage("Customer account created")
                    .build();
        } else {
            throw new RuntimeException("Failed to create customer");
        }
    }

    @Override
    public CustomerAccountResponseDto getCustomerByCustomerNumber(Long customerNumber) {
        Customer customer = customerRepository.findById(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        List<Account> accounts = customer.getAccounts() == null
                ? Collections.emptyList()
                : customer.getAccounts();

        List<AccountResponseDto> savings = accounts.stream()
                .filter(acc -> acc.getAccountType() == AccountType.SAVINGS)
                .map(this::mapToAccountResponseDto)
                .collect(Collectors.toList());

        List<AccountResponseDto> current = accounts.stream()
                .filter(acc -> acc.getAccountType() == AccountType.CHECKING)
                .map(this::mapToAccountResponseDto)
                .collect(Collectors.toList());

        return CustomerAccountResponseDto.builder()
                .customerNumber(customer.getId())
                .customerName(customer.getCustomerName())
                .customerMobile(customer.getCustomerMobile())
                .customerEmail(customer.getCustomerEmail())
                .address1(customer.getAddress1())
                .address2(customer.getAddress2())
                .savings(savings)
                .current(current)
                .transactionDescriptionMessage("Customer account found")
                .transactionStatusCode(HttpStatus.FOUND.value())
                .build();
    }

    private AccountResponseDto mapToAccountResponseDto(Account account) {
        return AccountResponseDto.builder()
                .customerId(account.getCustomer().getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }

}