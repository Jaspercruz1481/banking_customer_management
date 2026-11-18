package org.sample.customeraccountmanagement.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sample.customeraccountmanagement.account.entity.Account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDto {

    @NotBlank (message = "Name is required field")
    private String customerName;

    @NotBlank (message = "Mobile is required field")
    @Pattern(
            regexp = "^(09\\d{9}|\\+639\\d{9})$",
            message = "Mobile must be a valid Philippine mobile number (09XXXXXXXXX or +639XXXXXXXXX)"
    )
    private String customerMobile;

    @NotBlank(message = "Email is required field")
    @Email(message = "Email is not valid")
    private String customerEmail;

    @NotBlank(message = "Address 1  is required field")
    private String address1;
    private String address2;
}
