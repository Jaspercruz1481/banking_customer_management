package org.sample.customeraccountmanagement.account.service;

import org.sample.customeraccountmanagement.account.dto.AccountRequestDto;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;

public interface AccountService {

    AccountResponseDto createAccount(AccountRequestDto accountRequestDto);
}
