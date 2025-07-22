package com.niteshsynergy.service;

import com.niteshsynergy.entity.InternalCustomerPayments;
import com.niteshsynergy.model.InternalCustomers;
import com.niteshsynergy.model.PaymentResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface InternalCustomerService {

    /**
     * Find internal customer by phone number
     */
    Optional<InternalCustomers> findCustomerByPhone(String phone);

    /**
     * Delete internal customer by customerId
     */
    void deleteCustomerById(String customerId);

    /**
     * Asynchronous method to save internal customer with retry mechanism
     */
    @Async
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    CompletableFuture<InternalCustomers> saveInternalCustomer(InternalCustomers customer);

    /**
     * Save internal transaction with retry
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    InternalCustomerPayments saveInternalTransaction(InternalCustomers customer, PaymentResponse paymentResponse);

    /**
     * Process internal payment asynchronously
     */
    CompletableFuture<PaymentResponse> processInternalPayment(InternalCustomers customer, String upiId);

    /**
     * Find internal customer by ID
     */
    InternalCustomers findCustomerById(String customerId);
}
