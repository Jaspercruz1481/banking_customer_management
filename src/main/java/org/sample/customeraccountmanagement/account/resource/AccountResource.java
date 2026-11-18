package org.sample.customeraccountmanagement.account.resource;

import org.sample.customeraccountmanagement.account.dto.AccountRequestDto;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto requestDto) {
        AccountResponseDto createdAccount = accountService.createAccount(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

}
