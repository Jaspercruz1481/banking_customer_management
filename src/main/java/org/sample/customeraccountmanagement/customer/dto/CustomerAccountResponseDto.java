package org.sample.customeraccountmanagement.customer.dto;

import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerAccountResponseDto {

    private Long customerNumber;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String address1;
    private String address2;
    private List<AccountResponseDto> savings;
    private List<AccountResponseDto> current;
    private int transactionStatusCode;
    private String transactionDescriptionMessage;
}
