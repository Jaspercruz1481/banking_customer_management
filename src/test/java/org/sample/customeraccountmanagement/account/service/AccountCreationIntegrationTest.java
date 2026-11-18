package org.sample.customeraccountmanagement.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sample.customeraccountmanagement.account.dto.AccountRequestDto;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.account.entity.Account;
import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import org.sample.customeraccountmanagement.account.repository.AccountRepository;
import org.sample.customeraccountmanagement.customer.entity.Customer;
import org.sample.customeraccountmanagement.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:account-creation-integration;DB_CLOSE_DELAY=-1"
})
class AccountCreationIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE account_number_seq RESTART WITH 10000");
    }

    @Test
    void createsAndPersistsAccountsWithIncrementingSequenceNumbers() {
        Customer customer = customerRepository.save(Customer.builder()
                .customerName("Integration Test Customer")
                .customerMobile("09123456789")
                .customerEmail("integration@example.com")
                .address1("123 Test Street")
                .build());

        AccountResponseDto first = accountService.createAccount(createRequest(customer.getId(), AccountType.SAVINGS));
        AccountResponseDto second = accountService.createAccount(createRequest(customer.getId(), AccountType.CHECKING));

        assertEquals(10000L, first.getAccountNumber());
        assertEquals(10001L, second.getAccountNumber());

        List<Account> savedAccounts = accountRepository.findAll().stream()
                .filter(account -> account.getCustomer().getId().equals(customer.getId()))
                .collect(Collectors.toList());
        savedAccounts.sort(Comparator.comparing(Account::getAccountNumber));

        assertEquals(2, savedAccounts.size());
        assertEquals(10000L, savedAccounts.get(0).getAccountNumber());
        assertEquals(AccountType.SAVINGS, savedAccounts.get(0).getAccountType());
        assertEquals(new BigDecimal("500.00"), savedAccounts.get(0).getBalance());
        assertEquals(10001L, savedAccounts.get(1).getAccountNumber());
        assertEquals(AccountType.CHECKING, savedAccounts.get(1).getAccountType());
        assertEquals(new BigDecimal("500.00"), savedAccounts.get(1).getBalance());
    }

    private AccountRequestDto createRequest(Long customerId, AccountType accountType) {
        return AccountRequestDto.builder()
                .customerId(customerId)
                .accountType(accountType)
                .balance(new BigDecimal("500.00"))
                .build();
    }
}
