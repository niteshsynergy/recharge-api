package com.niteshsynergy.payment.razorpay;

import com.niteshsynergy.constant.RazorpayPaymentCred;
import com.niteshsynergy.payment.PaymentAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Autowired
    private PaymentAvailability paymentAvailability;  // To fetch Razorpay credentials from DB

    @Bean
    public RazorpayPaymentCred razorpayPaymentCred() {
        // Fetch Razorpay credentials from the database (via PaymentAvailability or another service)
        RazorpayPaymentCred razorpayCredentials = paymentAvailability.fetchRazorpayCredentials().join(); // Fetch from DB

        if (razorpayCredentials == null) {
            throw new RuntimeException("Razorpay credentials not found or inactive.");
        }

        return razorpayCredentials;  // Return the Razorpay credentials instance
    }
}
