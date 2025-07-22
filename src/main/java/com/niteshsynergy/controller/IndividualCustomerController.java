package com.niteshsynergy.controller;

import com.niteshsynergy.entity.IndividualCustomerPayments;
import com.niteshsynergy.exception.CustomerNotFoundException;
import com.niteshsynergy.exception.PaymentProcessException;
import com.niteshsynergy.model.IndividualCustomers;
import com.niteshsynergy.model.PaymentResponse;
import com.niteshsynergy.payment.upi.ValidUpiId;
import com.niteshsynergy.service.IndividualCustomerPaymentService;
import com.niteshsynergy.service.IndividualCustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.niteshsynergy.constant.PaymentConstant.*;

@RestController
@RequestMapping("/api/individual-customers")
@Validated
public class IndividualCustomerController {

    private static final Logger log = LoggerFactory.getLogger(IndividualCustomerController.class);

    @Autowired
    private IndividualCustomerService individualCustomerService;

    @Autowired
    private IndividualCustomerPaymentService individualCustomerPaymentService;


    @PostMapping("/save-customer")
    public ResponseEntity<IndividualCustomers> saveCustomer(@Valid @RequestBody IndividualCustomers customer) {
        CompletableFuture<IndividualCustomers> customerFuture = individualCustomerService.saveIndividualCustomer(customer);
        IndividualCustomers savedCustomer = customerFuture.join();
        return ResponseEntity.ok(savedCustomer);
    }

    @PostMapping("/process-payment/{customerId}")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable String customerId,
            @RequestParam @ValidUpiId String upiId,
            @RequestParam @NotBlank(message = "Channel Mode is required") String channelMode)
            throws CustomerNotFoundException {

        // Retrieve customer details
        IndividualCustomers savedCustomer = individualCustomerService.findCustomerById(customerId);

        if (savedCustomer == null) {
            throw new CustomerNotFoundException("Customer not found for ID: " + customerId);
        }

        PaymentResponse paymentResponse;
        CompletableFuture<PaymentResponse> paymentFuture;

        // Process payment based on channel mode
        if (PAYMENT_MODE_ONLINE.equalsIgnoreCase(channelMode)) {
            paymentFuture = individualCustomerService.processIndividualPayment(savedCustomer, upiId);
            paymentResponse = paymentFuture.join(); // Blocking call, waits for async completion
        }
        else if (PAYMENT_MODE_Cash.equalsIgnoreCase(channelMode)) {
            paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentMode(PAYMENT_MODE_Cash);
            paymentResponse.setPaymentDescription("Cash payment selected. Please verify the conditions.");
        }
        else {
            throw new IllegalArgumentException("Invalid channel mode: " + channelMode + ". Allowed values: 'Online' or 'Cash'.");
        }

        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/save-payment/{customerId}")
    public ResponseEntity<IndividualCustomerPayments> savePaymentTransaction(
            @PathVariable String customerId,
            @RequestBody PaymentResponse paymentResponse,String channelMode) throws CustomerNotFoundException {

        // Fetch customer details by customerId
        IndividualCustomers savedCustomer = individualCustomerService.findCustomerById(customerId);
        if (savedCustomer == null) {
            throw new CustomerNotFoundException("Customer not found for ID: " + customerId);
        }

        boolean paymentSuccess = false;

        // Payment success logic
        // write logic here even cash also then process savePaymentTransaction api. write logics also
        if (PAYMENT_MODE_ONLINE.equalsIgnoreCase(channelMode) ||
                (paymentResponse != null && SUCCESS.equalsIgnoreCase(paymentResponse.getStatus()))) {
            paymentSuccess = true;
        }

        // Save transaction record regardless of payment success or failure
        IndividualCustomerPayments savedPayment = individualCustomerService.saveIndividualTransaction(savedCustomer, paymentResponse);

        // If payment failed after retries, throw an exception (ensuring child record is saved first)
        if (!paymentSuccess) {
            throw new PaymentProcessException(
                    savedPayment.getTraceId(),
                    savedPayment.getTransactionId(),
                    customerId,
                    "Payment failed after 3 retries."
            );
        }

        return ResponseEntity.ok(savedPayment);
    }




    // ✅ Get Customer by Phone
    @GetMapping("/customer/phone/{phone}")
    public ResponseEntity<IndividualCustomers> getCustomerByPhone(@PathVariable String phone) {
        return ResponseEntity.of(individualCustomerService.findCustomerByPhone(phone));
    }

    // ✅ Get Transactions by Phone
    @GetMapping("/transactions/phone/{phone}")
    public ResponseEntity<List<IndividualCustomerPayments>> getTransactionsByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(individualCustomerPaymentService.findTransactionsByPhone(phone));
    }

    // ✅ Get Transactions by Email
    @GetMapping("/transactions/email/{email}")
    public ResponseEntity<List<IndividualCustomerPayments>> getTransactionsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(individualCustomerPaymentService.findTransactionsByEmail(email));
    }

    // ✅ Get Transactions by Status (0 or 1)
    @GetMapping("/transactions/status/{status}")
    public ResponseEntity<List<IndividualCustomerPayments>> getTransactionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(individualCustomerPaymentService.findTransactionsByStatus(status));
    }

    // ✅ Get Transactions by Order ID
    @GetMapping("/transactions/order/{orderId}")
    public ResponseEntity<List<IndividualCustomerPayments>> getTransactionsByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(individualCustomerPaymentService.findTransactionsByOrderId(orderId));
    }

    // ✅ Get Transactions by Transaction ID
    @GetMapping("/transactions/transaction/{transactionId}")
    public ResponseEntity<List<IndividualCustomerPayments>> getTransactionsByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(individualCustomerPaymentService.findTransactionsByTransactionId(transactionId));
    }
/*
    @PostMapping("/save")
    public ResponseEntity<IndividualCustomerPayments> processIndividualCustomer(@Valid @RequestBody IndividualCustomers customer) {
        String customerId = null;
        PaymentResponse paymentResponse = null;
        boolean paymentSuccess = false;
        // Save the customer and get the prepared customer object
        CompletableFuture<IndividualCustomers> customerFuture = individualCustomerService.saveIndividualCustomer(customer);
        IndividualCustomers savedCustomer = customerFuture.join(); // Block until customer is saved

        // Process payment (now includes retry logic inside)
        CompletableFuture<PaymentResponse> paymentFuture = null;
        if(PAYMENT_MODE_ONLINE.equalsIgnoreCase(customer.getChannelMode())) {
            paymentFuture = individualCustomerService.processIndividualPayment(savedCustomer);
            paymentResponse = paymentFuture.join();
        }

        if (PAYMENT_MODE_Cash.equalsIgnoreCase(customer.getChannelMode())
                || (paymentResponse != null && SUCCESS.equalsIgnoreCase(paymentResponse.getStatus())) )
        {
            paymentSuccess = true;
        }
        // Save final transaction record regardless of payment success or failure
        IndividualCustomerPayments savedPayment = individualCustomerService.saveIndividualTransaction(savedCustomer, paymentResponse);
        // If payment failed after retries, throw an exception (ensuring child record is saved first)
        if (!paymentSuccess) {
            throw new PaymentProcessException("traceId", "transactionId", customerId, "Payment failed after 3 retries.");
        }

        return ResponseEntity.ok(savedPayment);
    }*/
}