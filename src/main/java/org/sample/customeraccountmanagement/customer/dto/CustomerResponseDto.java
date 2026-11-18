package org.sample.customeraccountmanagement.customer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponseDto {

    private Long customerNumber;
    private int transactionStatusCode;
    private String transactionDescriptionMessage;
}
