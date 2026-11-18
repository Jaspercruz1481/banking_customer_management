package org.sample.customeraccountmanagement.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sample.customeraccountmanagement.account.entity.enums.AccountType;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be a positive number")
    private Long customerId;

    @NotNull(message = "Account type cannot be null")
    private AccountType accountType;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.00", message = "Balance must be zero or greater than zero")
    private BigDecimal balance;

}
