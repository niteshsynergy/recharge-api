package com.niteshsynergy.exception;

import com.niteshsynergy.exception.individual.IndividualCustomerException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private IndividualCustomerException customerException;

    // Handle validation errors (e.g., missing or invalid fields)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Handle custom validation exceptions
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleCustomValidationException(ValidationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle all other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Handle customer save errors
    @ExceptionHandler(CustomerSaveException.class)
    public ResponseEntity<MessageError> handleCustomerSaveError(CustomerSaveException ex) {
        MessageError error = customerException.saveIndividualCustomerError(ex.getTraceId(), ex.getTransactionId(), ex.getParentId(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle payment processing errors
    @ExceptionHandler(PaymentProcessException.class)
    public ResponseEntity<MessageError> handlePaymentProcessError(PaymentProcessException ex) {
        MessageError error = customerException.processIndividualPaymentError(ex.getTraceId(), ex.getTransactionId(), ex.getParentId(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle transaction save errors
    @ExceptionHandler(TransactionSaveException.class)
    public ResponseEntity<MessageError> handleTransactionSaveError(TransactionSaveException ex) {
        MessageError error = customerException.saveIndividualTransactionWithRetryError(ex.getTraceId(), ex.getTransactionId(), ex.getParentId(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
