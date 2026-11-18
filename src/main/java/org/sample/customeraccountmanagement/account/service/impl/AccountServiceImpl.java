package org.sample.customeraccountmanagement.account.service.impl;

import org.sample.customeraccountmanagement.account.dto.AccountRequestDto;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.account.entity.Account;
import org.sample.customeraccountmanagement.account.repository.AccountRepository;
import org.sample.customeraccountmanagement.account.service.AccountService;
import org.sample.customeraccountmanagement.common.exception.CustomerNotFoundException;
import org.sample.customeraccountmanagement.customer.entity.Customer;
import org.sample.customeraccountmanagement.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) {
        Customer customer = customerRepository.findById(accountRequestDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + accountRequestDto.getCustomerId()));

        Account account = Account.builder()
                .accountNumber(accountRepository.nextAccountNumber())
                .accountType(accountRequestDto.getAccountType())
                .balance(accountRequestDto.getBalance())
                .customer(customer)
                .build();

        Account saved = accountRepository.save(account);

        if (saved.getId() != null) {
            return AccountResponseDto.builder()
                    .customerId(saved.getCustomer().getId())
                    .accountNumber(saved.getAccountNumber())
                    .accountType(saved.getAccountType())
                    .balance(saved.getBalance())
                    .build();
        } else {
            throw new RuntimeException("Failed to create account");
        }

    }
}
