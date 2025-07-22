package com.niteshsynergy.controller;

import com.niteshsynergy.entity.InternalCustomerPayments;
import com.niteshsynergy.exception.CustomerNotFoundException;
import com.niteshsynergy.exception.PaymentProcessException;
import com.niteshsynergy.model.InternalCustomers;
import com.niteshsynergy.model.PaymentResponse;
import com.niteshsynergy.model.SubscriptionPlan;
import com.niteshsynergy.payment.upi.ValidUpiId;
import com.niteshsynergy.service.InternalCustomerPaymentService;
import com.niteshsynergy.service.InternalCustomerService;
import com.niteshsynergy.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.niteshsynergy.constant.PaymentConstant.*;

@RestController
@RequestMapping("/api/internal-customers")
@Validated
public class InternalCustomerController {

    private static final Logger log = LoggerFactory.getLogger(InternalCustomerController.class);

    @Autowired
    private InternalCustomerService internalCustomerService;

    @Autowired
    private InternalCustomerPaymentService internalCustomerPaymentService;

    // Add Autowiring for the new service
    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    // ✅ Save Internal Customer
    @PostMapping("/save-customer")
    public ResponseEntity<InternalCustomers> saveCustomer(@Valid @RequestBody InternalCustomers customer) {
        CompletableFuture<InternalCustomers> customerFuture = internalCustomerService.saveInternalCustomer(customer);
        InternalCustomers savedCustomer = customerFuture.join();
        return ResponseEntity.ok(savedCustomer);
    }

    // ✅ Process Payment
    @PostMapping("/process-payment/{customerId}")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable String customerId,
            @RequestParam @ValidUpiId String upiId,
            @RequestParam @NotBlank(message = "Channel Mode is required") String channelMode)
            throws CustomerNotFoundException {

        // Retrieve customer details
        InternalCustomers savedCustomer = internalCustomerService.findCustomerById(customerId);

        if (savedCustomer == null) {
            throw new CustomerNotFoundException("Internal Customer not found for ID: " + customerId);
        }


        PaymentResponse paymentResponse;
        CompletableFuture<PaymentResponse> paymentFuture;

        // Process payment based on channel mode
        if (PAYMENT_MODE_ONLINE.equalsIgnoreCase(channelMode)) {
            paymentFuture = internalCustomerService.processInternalPayment(savedCustomer, upiId);
            paymentResponse = paymentFuture.join(); // Blocking call
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

    // ✅ Save Payment Transaction (Both Success and Failure)
    @PostMapping("/save-payment/{customerId}")
    public ResponseEntity<InternalCustomerPayments> savePaymentTransaction(
            @PathVariable String customerId,
            @RequestBody PaymentResponse paymentResponse,
            String channelMode) throws CustomerNotFoundException {

        // Fetch customer details
        InternalCustomers savedCustomer = internalCustomerService.findCustomerById(customerId);
        if (savedCustomer == null) {
            throw new CustomerNotFoundException("Internal Customer not found for ID: " + customerId);
        }

        boolean paymentSuccess = false;

        // Payment success logic
        if (PAYMENT_MODE_ONLINE.equalsIgnoreCase(channelMode) ||
                (paymentResponse != null && SUCCESS.equalsIgnoreCase(paymentResponse.getStatus()))) {
            paymentSuccess = true;
        }

        // Save transaction record
        InternalCustomerPayments savedPayment = internalCustomerService.saveInternalTransaction(savedCustomer, paymentResponse);

        // If payment failed after retries, throw an exception
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


    // ✅ Get Internal Customer by Phone
    @GetMapping("/customer/phone/{phone}")
    public ResponseEntity<InternalCustomers> getCustomerByPhone(@PathVariable String phone) {
        return ResponseEntity.of(internalCustomerService.findCustomerByPhone(phone));
    }

    // ✅ Get Transactions by Phone
    @GetMapping("/transactions/phone/{phone}")
    public ResponseEntity<List<InternalCustomerPayments>> getTransactionsByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(internalCustomerPaymentService.findTransactionsByPhone(phone));
    }

    // ✅ Get Transactions by Email
    @GetMapping("/transactions/email/{email}")
    public ResponseEntity<List<InternalCustomerPayments>> getTransactionsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(internalCustomerPaymentService.findTransactionsByEmail(email));
    }

    // ✅ Get Transactions by Status (0 or 1)
    @GetMapping("/transactions/status/{status}")
    public ResponseEntity<List<InternalCustomerPayments>> getTransactionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(internalCustomerPaymentService.findTransactionsByStatus(status));
    }

    // ✅ Get Transactions by Order ID
    @GetMapping("/transactions/order/{orderId}")
    public ResponseEntity<List<InternalCustomerPayments>> getTransactionsByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(internalCustomerPaymentService.findTransactionsByOrderId(orderId));
    }

    // ✅ Get Transactions by Transaction ID
    @GetMapping("/transactions/transaction/{transactionId}")
    public ResponseEntity<List<InternalCustomerPayments>> getTransactionsByTransactionId(@PathVariable String transactionId) {
        return ResponseEntity.ok(internalCustomerPaymentService.findTransactionsByTransactionId(transactionId));
    }


    // this api will call only when playment done from or confirm from cash or online.
    // processPayment api sure respinse to UI.
    // before going to ccreateSubscription make sure customer have details in our db
    // payment done check it by  private String parentId; which will mentory input in
    //SubscriptionPlan input for createSubscription api I cant modfiy model as it will problem thier
    // maully update isPremium into InternalCustomers db record wheneevet this api call.
    // ✅ Create New Subscription Plan
    @PostMapping("/create-subscription")
    public ResponseEntity<SubscriptionPlan> createSubscription(@RequestBody SubscriptionPlan subscriptionRequest) {

        if (subscriptionRequest.getPhone() == null || subscriptionRequest.getCustomerId() == null) {
            throw new IllegalArgumentException("Phone and CustomerId are required.");
        }
        // max 1 plan allow to take not more if user have alread plant hen dont take new reuqest.
        // omceplan created to call
        /*
         private int isPremium;
& update in InternalCustomers as taken plan.
processPayment
         */
        // Set Expiry Date to 1 Month Later (Example)
        subscriptionRequest.setExpiryDate(LocalDateTime.now().plusMonths(1));

        CompletableFuture<SubscriptionPlan> planFuture = subscriptionPlanService.createSubscription(subscriptionRequest);
        SubscriptionPlan savedPlan = planFuture.join();

        log.info("✅ Subscription created with Transaction ID: {}", savedPlan.getTransactionId());
        return ResponseEntity.ok(savedPlan);
    }

    // ✅ Get Plan Status by Phone
    @GetMapping("/subscription/status/{phone}")
    public ResponseEntity<List<SubscriptionPlan>> getPlanStatusByPhone(@PathVariable String phone) {
        CompletableFuture<List<SubscriptionPlan>> planFuture = subscriptionPlanService.getPlanStatusByPhone(phone);
        List<SubscriptionPlan> plans = planFuture.join();

        if (plans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(plans);
    }


}
