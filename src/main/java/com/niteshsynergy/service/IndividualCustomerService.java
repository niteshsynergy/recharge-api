package com.niteshsynergy.service;

import com.niteshsynergy.entity.IndividualCustomerPayments;
import com.niteshsynergy.model.IndividualCustomers;
import com.niteshsynergy.model.PaymentResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface IndividualCustomerService {

    Optional<IndividualCustomers> findCustomerByPhone(String phone);

    void deleteCustomerById(String customerId);

    @Async
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public CompletableFuture<IndividualCustomers> saveIndividualCustomer(IndividualCustomers customer);


    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public IndividualCustomerPayments saveIndividualTransaction(IndividualCustomers customer, PaymentResponse paymentResponse);


    public  CompletableFuture<PaymentResponse> processIndividualPayment(IndividualCustomers customer,String upiId);

    public IndividualCustomers findCustomerById(String customerId) ;
}
