package org.sample.customeraccountmanagement.common.handler;

import org.sample.customeraccountmanagement.common.exception.AccountNotFoundException;
import org.sample.customeraccountmanagement.common.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Request validation failed");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("transactionStatusCode", HttpStatus.BAD_REQUEST.value());
        body.put("transactionStatusDescription", message);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(
            CustomerNotFoundException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("transactionStatusCode", HttpStatus.NOT_FOUND.value());
        body.put("transactionStatusDescription", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFound(
            AccountNotFoundException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("transactionStatusCode", HttpStatus.NOT_FOUND.value());
        body.put("transactionStatusDescription", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


}
