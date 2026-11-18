package org.sample.customeraccountmanagement.customer.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sample.customeraccountmanagement.account.dto.AccountResponseDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerAccountResponseDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerRequestDto;
import org.sample.customeraccountmanagement.customer.dto.CustomerResponseDto;
import org.sample.customeraccountmanagement.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerResourceTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerResource customerResource;


    @Test
    void createCustomer() {
        CustomerRequestDto request = createRequestDto();

        CustomerResponseDto expected = CustomerResponseDto.builder()
                .customerNumber(1L)
                .transactionDescriptionMessage("Customer created successfully")
                .transactionStatusCode(200)
                .build();

        when(customerService.createCustomer(request)).thenReturn(expected);

        ResponseEntity<CustomerResponseDto> response = customerResource.createCustomer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(expected, response.getBody());
        verify(customerService).createCustomer(request);
    }

    @Test
    void getCustomerByCustomerNumber() {
        Long customerNumber = 1L;

        CustomerAccountResponseDto expected = CustomerAccountResponseDto.builder()
                .customerNumber(customerNumber)
                .customerName("John")
                .customerMobile("0912345678")
                .customerEmail("john.doe@example.com")
                .address1("123 Main St")
                .address2("Apt 4B")
                .build();

        when(customerService.getCustomerByCustomerNumber(customerNumber)).thenReturn(expected);

        ResponseEntity<CustomerAccountResponseDto> response = customerResource.getCustomerByCustomerNumber(customerNumber);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertSame(expected, response.getBody());
        verify(customerService).getCustomerByCustomerNumber(customerNumber);
    }

    private CustomerRequestDto createRequestDto() {
        return CustomerRequestDto.builder()
                .customerName("John")
                .customerMobile("0912345678")
                .customerEmail("john.doe@example.com")
                .address1("123 Main St")
                .address2("Apt 4B")
                .build();
    }
}