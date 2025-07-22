package com.niteshsynergy.service.impl.internal;

import com.niteshsynergy.constant.RazorpayPaymentCred;
import com.niteshsynergy.entity.InternalCustomerPayments;
import com.niteshsynergy.entity.PaymentOptions;
import com.niteshsynergy.exception.CustomerSaveException;
import com.niteshsynergy.exception.TransactionSaveException;
import com.niteshsynergy.model.InternalCustomers;
import com.niteshsynergy.model.PaymentRequest;
import com.niteshsynergy.model.PaymentResponse;
import com.niteshsynergy.payment.PaymentAvailability;
import com.niteshsynergy.service.InternalCustomerService;
import com.niteshsynergy.utils.OrderTransactionUtils;
import com.niteshsynergy.utils.PaymentProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.niteshsynergy.constant.PaymentConstant.*;
import static com.niteshsynergy.constant.RazorPayConstant.RAZORPAY;
import static com.niteshsynergy.utils.internal.PrepareInternalCustomersData.prepareInternalCustomersData;
import static com.niteshsynergy.utils.razorpay.RazorpayInputOutputPrepare.*;

@Service
public class InternalCustomerServiceImpl implements InternalCustomerService {

    @Autowired
    private MongoTemplate mongoTemplate;

    Logger log = LoggerFactory.getLogger(InternalCustomerServiceImpl.class);

    @Autowired
    private PaymentProcess paymentProcess;

    @Autowired
    private PaymentAvailability paymentAvailability;

    @Override
    public void deleteCustomerById(String customerId) {
        Query query = new Query(Criteria.where("id").is(customerId));
        InternalCustomers customer = mongoTemplate.findOne(query, InternalCustomers.class);

        if (customer != null) {
            mongoTemplate.remove(customer);
            log.info("Internal Customer with ID: {} deleted successfully.", customerId);
        } else {
            log.warn("Internal Customer with ID: {} does not exist.", customerId);
        }
    }

    @Override
    public Optional<InternalCustomers> findCustomerByPhone(String phone) {
        Query query = new Query(Criteria.where("phone").is(phone));
        return Optional.ofNullable(mongoTemplate.findOne(query, InternalCustomers.class));
    }

    @Async
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public CompletableFuture<InternalCustomers> saveInternalCustomer(InternalCustomers customer) {
        double amount = customer.getAmount();
        InternalCustomers preparedCustomer = prepareInternalCustomersData(customer);

        return CompletableFuture.supplyAsync(() -> {
            try {
                InternalCustomers existingCustomer = mongoTemplate.findOne(
                        new Query(Criteria.where("phone").is(preparedCustomer.getPhone())),
                        InternalCustomers.class
                );

                return (existingCustomer != null) ? existingCustomer : mongoTemplate.save(preparedCustomer);
            } catch (Exception e) {
                log.error("Error saving internal customer: {}, TraceId: {}, ParentId: {}", e.getMessage(), customer.getTraceId(), customer.getParentId());
                throw new CustomerSaveException(customer.getTraceId(), customer.getParentId(), e.getMessage());
            }
        }).thenApply(savedCustomer -> {
            if (savedCustomer == null) {
                throw new CustomerSaveException(customer.getTraceId(), customer.getParentId(), "Failed to save or fetch internal customer.");
            }
            savedCustomer.setAmount(amount);
            return savedCustomer;
        });
    }

    public CompletableFuture<PaymentResponse> processInternalPayment(InternalCustomers customer, String upiId) {
        try {
            List<PaymentOptions> paymentOptions = paymentAvailability.fetchPaymentOptionsByChannelType(customer.getChannelType());
            CompletableFuture<RazorpayPaymentCred> razorpayCredentialsFuture = paymentAvailability.fetchRazorpayCredentials();

            String orderId = OrderTransactionUtils.generateOrderId(customer.getClientName(), customer.getChannelType(), PAYMENT_MODE_ONLINE);
            String transactionId = OrderTransactionUtils.generateTransactionId(customer.getClientName(), customer.getChannelType(), PAYMENT_MODE_ONLINE);

            return razorpayCredentialsFuture.thenCompose(razorpayCredentials -> {
                if (razorpayCredentials == null || razorpayCredentials.getKeyId() == null || razorpayCredentials.getKeySecret() == null) {
                    return CompletableFuture.completedFuture(prepareFailedResponse(transactionId, orderId, customer.getAmount(),
                            customer.getEmail(), customer.getPhone().toString(), "RAZORPAY_ERROR", "Razorpay credentials are missing or invalid", null));
                }

                boolean isRazorpayAvailable = paymentOptions.stream()
                        .anyMatch(option -> "Razorpay".equalsIgnoreCase(option.getPaymentMethod()));

                if (!isRazorpayAvailable) {
                    return CompletableFuture.completedFuture(prepareFailedResponse(transactionId, orderId, customer.getAmount(),
                            customer.getEmail(), customer.getPhone().toString(), "PAYMENT_NOT_SUPPORTED", "Razorpay is not enabled", null));
                }

                PaymentRequest paymentRequest = preparePaymentInput(orderId, transactionId, customer.getAmount(), customer.getEmail(), razorpayCredentials, upiId);

                return CompletableFuture.supplyAsync(() -> {
                    PaymentResponse paymentResponse = paymentProcess.initiatePayment(paymentRequest, RAZORPAY);
                    return paymentResponse;
                });

            });
        } catch (Exception e) {
            log.error("Error processing internal payment: {}", e.getMessage());
            return CompletableFuture.completedFuture(new PaymentResponse());
        }
    }

    @Override
    public InternalCustomers findCustomerById(String customerId) {
        return mongoTemplate.findById(customerId, InternalCustomers.class);
    }

    private InternalCustomerPayments saveFinalTransaction(InternalCustomers customer, PaymentResponse paymentResponse) {
        if (customer.getId() == null) {
            log.error("Customer ID is null. Cannot save payment.");
            throw new IllegalStateException("Customer ID is null.");
        }

        InternalCustomerPayments payment = InternalCustomerPayments.builder()
                .amount(customer.getAmount())
                .parentId(customer.getParentId())
                .transactionId(paymentResponse.getTransactionId())
                .orderId(paymentResponse.getOrderId())
                .paymentMode("ONLINE")
                .paymentGateway("RAZORPAY")
                .paymentStatus(paymentResponse.getStatus())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        InternalCustomerPayments savedPayment = mongoTemplate.save(payment);
        log.info("Payment saved with parentId: " + savedPayment.getParentId());

        updateParentAmount(customer.getId(), customer.getAmount());

        return savedPayment;
    }

    private void updateParentAmount(String parentId, Double childAmount) {
        InternalCustomers parentCustomer = mongoTemplate.findById(parentId, InternalCustomers.class);

        if (parentCustomer == null) {
            log.error("Parent customer with ID " + parentId + " not found.");
            throw new IllegalStateException("Parent customer not found.");
        }

        Double existingAmount = parentCustomer.getAmount();
        Double updatedAmount = existingAmount + childAmount;

        parentCustomer.setAmount(updatedAmount);
        mongoTemplate.save(parentCustomer);

        log.info("Parent amount updated. New amount: " + updatedAmount);
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public InternalCustomerPayments saveInternalTransaction(InternalCustomers customer, PaymentResponse paymentResponse) {
        try {
            return saveFinalTransaction(customer, paymentResponse);
        } catch (Exception e) {
            throw new TransactionSaveException("traceId", "transactionId", customer.getId(), e.getMessage());
        }
    }

    @Recover
    public void handleCustomerSaveFailure(String customerId) {
        System.out.println("Failed to save internal customer with ID: " + customerId + ". Please retry.");
    }
}
