package org.sample.customeraccountmanagement.customer.resource;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sample.customeraccountmanagement.common.handler.ApiExceptionHandler;
import org.sample.customeraccountmanagement.common.exception.CustomerNotFoundException;
import org.sample.customeraccountmanagement.customer.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CustomerResourceExceptionTest {

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CustomerResource customerResource = new CustomerResource(customerService);

        mockMvc = MockMvcBuilders.standaloneSetup(customerResource)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void createCustomerReturnsBadRequestForEmptyEmail() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"customerName\":\"John\","
                                + "\"customerMobile\":\"09123456781\","
                                + "\"customerEmail\":\"\","
                                + "\"address1\":\"123 Main St\""
                                + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400))
                .andExpect(jsonPath("$.transactionStatusDescription")
                        .value("Email is required field"));

    }

    @Test
    void createCustomerReturnsBadRequestForInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"customerName\":\"John\","
                                + "\"customerMobile\":\"09129971774\","
                                + "\"customerEmail\":\"johndoe\","
                                + "\"address1\":\"123 Main St\""
                                + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400))
                .andExpect(jsonPath("$.transactionStatusDescription")
                        .value("Email is not valid"));

    }

    @Test
    void getCustomerReturnsCustomNotFoundResponse() throws Exception {
        when(customerService.getCustomerByCustomerNumber(999L))
                .thenThrow(new CustomerNotFoundException(
                        "Customer not found with ID: 999"));

        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.transactionStatusCode").value(404))
                .andExpect(jsonPath("$.transactionStatusDescription")
                        .value("Customer not found with ID: 999"));
    }
}
