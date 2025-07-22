package com.niteshsynergy.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    // Transaction details
    private String transactionId;
    private String orderId;

    // Status of the payment
    private String status;   // Possible values: "Success", "Pending", "Failed"
    private String statusCode;   // Optional: A status code if applicable (can be used for detailed errors)

    // Payment details
    private String paymentGateway;
    private String paymentMode;    // Payment method (Credit/Debit Card, Net Banking, etc.)
    private double amount;
    private String currency;

    // Customer details
    private String email;
    private String phoneNumber;

    // Card details (if applicable)
    private String cardNumber;
    private String cardExpiry;
    private String cardType;

    // Error details if the payment fails
    private String errorCode;     // Error code returned from gateway (e.g., "INSUFFICIENT_FUNDS")
    private String errorMessage;  // Error message returned from gateway

    // Refund details (if applicable)
    private String refundStatus;   // Refund status if the payment is refunded (e.g., "Refunded", "Failed")
    private String refundTransactionId;  // Transaction ID for the refund (if applicable)

    // Payment metadata or additional information
    private String paymentDescription;

    private String message;  // This is the message to be set based on payment status

    // Constructor for easy initialization
    public PaymentResponse(String transactionId, String orderId, String status, String paymentGateway, double amount, String currency, String email, String phoneNumber) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.status = status;
        this.paymentGateway = paymentGateway;
        this.amount = amount;
        this.currency = currency;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
