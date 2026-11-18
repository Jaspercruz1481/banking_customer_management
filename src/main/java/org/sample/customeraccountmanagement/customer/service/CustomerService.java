package org.sample.customeraccountmanagement.customer.service;

import org.sample.customeraccountmanagement.customer.dto.CustomerAccountResponseDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerRequestDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerResponseDto;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);

    CustomerAccountResponseDto getCustomerByCustomerNumber(Long customerNumber);


}