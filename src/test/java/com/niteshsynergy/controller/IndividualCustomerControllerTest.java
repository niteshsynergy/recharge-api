package com.niteshsynergy.controller;

import com.niteshsynergy.entity.IndividualCustomerPayments;
import com.niteshsynergy.exception.PaymentProcessException;
import com.niteshsynergy.model.IndividualCustomers;
import com.niteshsynergy.model.PaymentRequest;
import com.niteshsynergy.model.PaymentResponse;
import com.niteshsynergy.service.IndividualCustomerPaymentService;
import com.niteshsynergy.service.IndividualCustomerService;
import com.niteshsynergy.utils.PaymentProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.niteshsynergy.constant.PaymentConstant.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndividualCustomerControllerTest {

    @InjectMocks
    private IndividualCustomerController individualCustomerController;

    @Mock
    private IndividualCustomerService individualCustomerService;

    @Mock
    private IndividualCustomerPaymentService individualCustomerPaymentService;

    @Mock
    private PaymentProcess paymentProcess;

    @Mock
    private TransactionTemplate transactionTemplate;

    private IndividualCustomers testCustomer;
    private PaymentResponse testPaymentResponse;

    @BeforeEach
    void setUp() {
        testCustomer = new IndividualCustomers();
        testCustomer.setId(UUID.randomUUID().toString());
        testCustomer.setEmail("test@example.com");
        testCustomer.setAmount(100.0);

        testPaymentResponse = new PaymentResponse();
        testPaymentResponse.setTransactionId(UUID.randomUUID().toString());
        testPaymentResponse.setOrderId(UUID.randomUUID().toString());
        testPaymentResponse.setStatus(SUCCESS);
    }

    @Test
    void testProcessIndividualCustomer_Success() {
        // Mock customer saving
        when(individualCustomerService.findCustomerByPhone(any()))
                .thenReturn(Optional.of(testCustomer));

        // Mock payment process
        when(paymentProcess.initiatePayment(any(PaymentRequest.class), any()))
                .thenReturn(testPaymentResponse);

        // Mock transaction save
        IndividualCustomerPayments paymentRecord = new IndividualCustomerPayments();
        when(individualCustomerPaymentService.savePaymentTransaction(any()))
                .thenReturn(paymentRecord);

        // Execute method
        ResponseEntity<IndividualCustomerPayments> response =
                individualCustomerController.processIndividualCustomer(testCustomer);

        // Validate response
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(individualCustomerService, times(1)).findCustomerByPhone(any());
        verify(paymentProcess, times(1)).initiatePayment(any(PaymentRequest.class), any());
        verify(individualCustomerPaymentService, times(1)).savePaymentTransaction(any());
    }

    @Test
    void testProcessIndividualCustomer_PaymentFailure() {
        // Mock customer saving
        when(individualCustomerService.findCustomerByPhone(any()))
                .thenReturn(Optional.of(testCustomer));

        // Mock payment failure
        when(paymentProcess.initiatePayment(any(PaymentRequest.class), any()))
                .thenThrow(new PaymentProcessException("traceId", "transactionId", "parentId", "Payment failed"));

        // Expect exception
        assertThrows(PaymentProcessException.class, () ->
                individualCustomerController.processIndividualCustomer(testCustomer));

        verify(individualCustomerService, times(1)).findCustomerByPhone(any());
        verify(paymentProcess, times(1)).initiatePayment(any(PaymentRequest.class), any());
    }

    @Test
    void testGetCustomerByPhone() {
        when(individualCustomerService.findCustomerByPhone("1234567890"))
                .thenReturn(Optional.of(testCustomer));

        ResponseEntity<IndividualCustomers> response = individualCustomerController.getCustomerByPhone("1234567890");

        assertTrue(response.hasBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testCustomer.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testGetCustomerByPhone_NotFound() {
        when(individualCustomerService.findCustomerByPhone("0000000000"))
                .thenReturn(Optional.empty());

        ResponseEntity<IndividualCustomers> response = individualCustomerController.getCustomerByPhone("0000000000");

        assertFalse(response.hasBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
