package org.sample.customeraccountmanagement.customer.resource;

import org.sample.customeraccountmanagement.customer.dto.CustomerAccountResponseDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerRequestDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerResponseDto;
import org.sample.customeraccountmanagement.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto requestDto) {
        CustomerResponseDto createdAccount = customerService.createCustomer(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerAccountResponseDto> getCustomerByCustomerNumber(@PathVariable Long customerNumber) {
        CustomerAccountResponseDto customer = customerService.getCustomerByCustomerNumber(customerNumber);
        return ResponseEntity.status(HttpStatus.FOUND).body(customer);
    }

}
