package org.sample.customeraccountmanagement.account.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sample.customeraccountmanagement.account.dto.AccountRequestDto;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import org.sample.customeraccountmanagement.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountResourceTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountResource accountResource;

    @Test
    void testCreateAccount() {
        AccountRequestDto request = AccountRequestDto.builder()
                .customerId(1L)
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("500.00"))
                .build();

        AccountResponseDto expected = AccountResponseDto.builder()
                .customerId(1L)
                .accountNumber(1001L)
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("500.00"))
                .build();

        when(accountService.createAccount(request)).thenReturn(expected);

        ResponseEntity<AccountResponseDto> response = accountResource.createAccount(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(expected, response.getBody());
        verify(accountService).createAccount(request);
    }

}