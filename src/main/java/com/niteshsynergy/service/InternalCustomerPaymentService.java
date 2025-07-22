package com.niteshsynergy.service;

import com.niteshsynergy.entity.InternalCustomerPayments;

import java.util.List;

public interface InternalCustomerPaymentService {
    InternalCustomerPayments savePaymentTransaction(InternalCustomerPayments payment);

    List<InternalCustomerPayments> findTransactionsByPhone(String phone);

    List<InternalCustomerPayments> findTransactionsByEmail(String email);

    List<InternalCustomerPayments> findTransactionsByStatus(String status);

    List<InternalCustomerPayments> findTransactionsByOrderId(String orderId);

    List<InternalCustomerPayments> findTransactionsByTransactionId(String transactionId);

    void deletePaymentByTransactionId(String transactionId);
}
