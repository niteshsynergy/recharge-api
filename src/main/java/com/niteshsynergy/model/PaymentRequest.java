package com.niteshsynergy.model;

import com.niteshsynergy.payment.upi.ValidUpiId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private String orderId;
    private String transactionId;
    private double amount;
    private String currency; // Default currency
    private String email;
    private String keyId; // Razorpay Key ID
    private String apiEndpointUrl; // Razorpay API Endpoint
    private String keySecret; // Razorpay Key Secret for authentication

    @NotBlank(message = "UPI ID cannot be blank")
    @ValidUpiId // ✅ Custom UPI ID validation using Enum
    private String upiId;

    // Constructor for Razorpay Payment Request
    public PaymentRequest(String orderId, String transactionId, @PositiveOrZero(message = "Amount cannot be negative") double amount,
                          String email, String keyId, String apiEndpointUrl, String keySecret) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.email = email;
        this.keyId = keyId;
        this.apiEndpointUrl = apiEndpointUrl;
        this.keySecret = keySecret;
    }
}
