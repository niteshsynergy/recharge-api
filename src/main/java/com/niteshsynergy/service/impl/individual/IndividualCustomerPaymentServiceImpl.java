package com.niteshsynergy.service.impl.individual;

import com.niteshsynergy.entity.IndividualCustomerPayments;
import com.niteshsynergy.service.IndividualCustomerPaymentService;
import com.niteshsynergy.model.PaymentResponse;
import com.niteshsynergy.utils.PaymentProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class IndividualCustomerPaymentServiceImpl implements IndividualCustomerPaymentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PaymentProcess paymentProcess;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int RETRY_DELAY = 2000; // 2 seconds

    @Override
    @Transactional
    public IndividualCustomerPayments savePaymentTransaction(IndividualCustomerPayments payment) {
        return mongoTemplate.save(payment);
    }

    @Override
    public List<IndividualCustomerPayments> findTransactionsByPhone(String phone) {
        Query query = new Query(Criteria.where("phone").is(phone));
        return mongoTemplate.find(query, IndividualCustomerPayments.class);
    }

    @Override
    public List<IndividualCustomerPayments> findTransactionsByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.find(query, IndividualCustomerPayments.class);
    }

    @Override
    public List<IndividualCustomerPayments> findTransactionsByStatus(String status) {
        Query query = new Query(Criteria.where("paymentStatus").is(status));
        return mongoTemplate.find(query, IndividualCustomerPayments.class);
    }

    @Override
    public List<IndividualCustomerPayments> findTransactionsByOrderId(String orderId) {
        Query query = new Query(Criteria.where("orderId").is(orderId));
        return mongoTemplate.find(query, IndividualCustomerPayments.class);
    }

    @Override
    public List<IndividualCustomerPayments> findTransactionsByTransactionId(String transactionId) {
        Query query = new Query(Criteria.where("transactionId").is(transactionId));
        return mongoTemplate.find(query, IndividualCustomerPayments.class);
    }

    /**
     * Method to retry the transaction and save the payment
     */
    @Retryable(value = Exception.class, maxAttempts = MAX_RETRY_ATTEMPTS, backoff = @Backoff(delay = RETRY_DELAY))
    public IndividualCustomerPayments savePaymentTransactionWithRetry(IndividualCustomerPayments payment) {
        return saveFinalTransaction(payment);
    }

    // Final transaction save after retry
    public IndividualCustomerPayments saveFinalTransaction(IndividualCustomerPayments payment) {
        payment.setCreatedOn(LocalDateTime.now());
        payment.setUpdatedOn(LocalDateTime.now());
        return mongoTemplate.save(payment);
    }

    // Handle the refund logic if payment still pending
    @Recover
    public void handleRefund(String orderId, String transactionId, double amount) {
        // Logic to refund the money
        System.out.println("Refunding money for orderId: " + orderId + " and transactionId: " + transactionId);
    }

    // Retry logic for pending payment status
    /*public void checkPaymentStatusAndRetry(String orderId, String transactionId) {
        LocalDateTime startTime = LocalDateTime.now();
        while (java.time.Duration.between(startTime, LocalDateTime.now()).toMinutes() < 5) {
            PaymentResponse paymentResponse = paymentProcess.checkPaymentStatus(orderId, transactionId);
            if ("SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
                // Payment successful, save the transaction
                IndividualCustomerPayments payment = new IndividualCustomerPayments();
                payment.setTransactionId(paymentResponse.getTransactionId());
                payment.setPaymentStatus(paymentResponse.getStatus());
                saveFinalTransaction(payment);
                return;
            } else if ("PENDING".equalsIgnoreCase(paymentResponse.getStatus())) {
                try {
                    Thread.sleep(2000); // Retry after 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        handleRefund(orderId, transactionId, 0.0);
    }*/

    /**
     * ❌ Method to delete a payment by transaction ID (used in rollback)
     */
    public void deletePaymentByTransactionId(String transactionId) {
        Query query = new Query(Criteria.where("transactionId").is(transactionId));
        IndividualCustomerPayments payment = mongoTemplate.findOne(query, IndividualCustomerPayments.class);

        if (payment != null) {
            mongoTemplate.remove(payment);
            System.out.println("Payment with Transaction ID: " + transactionId + " deleted successfully.");
        } else {
            System.out.println("Payment with Transaction ID: " + transactionId + " does not exist.");
        }
    }
}
