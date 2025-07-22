package com.niteshsynergy.service;

import com.niteshsynergy.entity.IndividualCustomerPayments;

import java.util.List;

public interface IndividualCustomerPaymentService {
    IndividualCustomerPayments savePaymentTransaction(IndividualCustomerPayments payment);

    List<IndividualCustomerPayments> findTransactionsByPhone(String phone);

    List<IndividualCustomerPayments> findTransactionsByEmail(String email);

    List<IndividualCustomerPayments> findTransactionsByStatus(String status);

    List<IndividualCustomerPayments> findTransactionsByOrderId(String orderId);

    List<IndividualCustomerPayments> findTransactionsByTransactionId(String transactionId);

    void deletePaymentByTransactionId(String transactionId);
}
