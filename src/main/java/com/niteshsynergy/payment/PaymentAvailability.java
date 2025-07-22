package com.niteshsynergy.payment;

import com.niteshsynergy.constant.RazorpayPaymentCred;
import com.niteshsynergy.entity.PaymentOptions;
import com.niteshsynergy.utils.razorpay.RazorpayInputOutputPrepare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentAvailability {

    @Autowired
    private MongoTemplate mongoTemplate;  // Injecting MongoTemplate correctly (no static)

    @Autowired
    private RazorpayInputOutputPrepare razorpayInputPrepare;  // Injecting RazorpayInputOutputPrepare

    // Method to fetch payment options based on the channel type
    public  List<PaymentOptions> fetchPaymentOptionsByChannelType(String channelType) {
        List<PaymentOptions> paymentOptions = new ArrayList<>();

        if ("Individual".equalsIgnoreCase(channelType)) {  // Hardcoded for "Individual"
            PaymentOptions razorpay = new PaymentOptions("Razorpay", "Online", "Razorpay Payment Gateway", 1);
            paymentOptions.add(razorpay); // Add Razorpay as a hardcoded option for "individual"
        } else {
            // Dynamically fetching payment options for other channel types (e.g., payment, etc.)
            paymentOptions = mongoTemplate.findAll(PaymentOptions.class);
        }

        return paymentOptions;
    }

/*    // Method to fetch Razorpay credentials from MongoDB based on payment method and status = 1
    public  CompletableFuture<RazorpayPaymentCred> fetchRazorpayCredentials() {
        return CompletableFuture.supplyAsync(() -> {
            // Query to find Razorpay credentials where paymentMethod = "Razorpay" and status = 1
            RazorpayPaymentCred razorpayCredentials = mongoTemplate.findOne(
                    new org.springframework.data.mongodb.core.query.Query(
                            org.springframework.data.mongodb.core.query.Criteria.where("paymentMethod").is("Razorpay")
                                    .and("status").is(1)
                    ),
                    RazorpayPaymentCred.class // Mapping to RazorpayPaymentCred class
            );

            if (razorpayCredentials == null) {
                throw new RuntimeException("Razorpay credentials not available or inactive.");
            }

            return razorpayCredentials;  // Return the fetched Razorpay credentials
        });
    }*/

    public CompletableFuture<RazorpayPaymentCred> fetchRazorpayCredentials() {
        return CompletableFuture.supplyAsync(() -> {
            // Using Builder to create RazorpayPaymentCred with hard-coded values
            RazorpayPaymentCred razorpayCredentials = RazorpayPaymentCred.builder()
                    .merchantId("PM4VNcLlaHhWA8")
                    .keyId("rzp_test_B2jQOjxoFwW4Sq")
                    .keySecret("jyhyCtv4xnC9zppKRSEssqod")
                    .webhookSecret("your-webhook-secret-here")
                    .publicKey("your-public-key-here")
                    .apiEndpointUrl("https://api.razorpay.com/v1/")
                    .build();

            // Simulate a check to make sure the credentials are valid (status = 1)
            if (razorpayCredentials == null || razorpayCredentials.getKeyId() == null || razorpayCredentials.getKeySecret() == null) {
                throw new RuntimeException("Razorpay credentials not available or inactive.");
            }

            return razorpayCredentials;
        });
    }

}
