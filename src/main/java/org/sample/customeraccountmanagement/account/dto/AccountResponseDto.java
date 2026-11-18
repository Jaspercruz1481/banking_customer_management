package org.sample.customeraccountmanagement.account.dto;

import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponseDto {

    private Long customerId;
    private Long accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
}
