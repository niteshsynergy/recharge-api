package com.niteshsynergy.utils.razorpay;

import com.niteshsynergy.constant.RazorpayPaymentCred;
import com.niteshsynergy.model.PaymentRequest;
import com.niteshsynergy.model.PaymentResponse;
import org.springframework.stereotype.Component;

import static com.niteshsynergy.constant.PaymentConstant.*;

@Component
public class RazorpayInputOutputPrepare {

    // Method to prepare input for Razorpay
    public static PaymentRequest preparePaymentInput(String orderId, String transactionId, double amount,
                                              String customerEmail, RazorpayPaymentCred razorpayCredentials,String upiId) {
        return PaymentRequest.builder()
                .transactionId(transactionId)
                .amount(amount)
                .currency("INR")
                .email(customerEmail)
                .keyId(razorpayCredentials.getKeyId())
                .apiEndpointUrl(razorpayCredentials.getApiEndpointUrl())
                .keySecret(razorpayCredentials.getKeySecret())
                .upiId(upiId)
                .build();
    }

    // Method to prepare successful payment response
    public static PaymentResponse prepareSuccessResponse(String transactionId, String orderId, double amount,
                                                         String customerEmail, String customerPhone, RazorpayPaymentCred razorpayCredentials) {
        return PaymentResponse.builder()
                .transactionId(transactionId)
                .orderId(orderId)
                .status(STATUS_SUCCESS)
                .amount(amount)
                .currency("INR")
                .email(customerEmail)
                .phoneNumber(customerPhone)
                .paymentGateway("Razorpay")
                .paymentMode("Card")
                .build();
    }

    // Method to prepare pending payment response
    public static PaymentResponse preparePendingResponse(String transactionId, String orderId, double amount,
                                                         String customerEmail, String customerPhone, RazorpayPaymentCred razorpayCredentials) {
        return PaymentResponse.builder()
                .transactionId(transactionId)
                .orderId(orderId)
                .status(STATUS_PENDING)
                .amount(amount)
                .currency("INR")
                .email(customerEmail)
                .phoneNumber(customerPhone)
                .paymentGateway("Razorpay")
                .paymentMode("Card")
                .build();
    }

    // Method to prepare failed payment response
    public static PaymentResponse prepareFailedResponse(String transactionId, String orderId, double amount,
                                                        String customerEmail, String customerPhone, String errorCode,
                                                        String errorMessage, RazorpayPaymentCred razorpayCredentials) {
        return PaymentResponse.builder()
                .transactionId(transactionId)
                .orderId(orderId)
                .status(STATUS_FAILED)
                .amount(amount)
                .currency("INR")
                .email(customerEmail)
                .phoneNumber(customerPhone)
                .paymentGateway("Razorpay")
                .paymentMode("Card")
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
    // Method to prepare failed payment response
    public static PaymentResponse prepareRefundedResponse(String transactionId, String orderId, double amount,
                                                        String customerEmail, String customerPhone, String errorCode,
                                                        String errorMessage, RazorpayPaymentCred razorpayCredentials) {
        return PaymentResponse.builder()
                .transactionId(transactionId)
                .orderId(orderId)
                .status(STATUS_REFUNDED)
                .amount(amount)
                .currency("INR")
                .email(customerEmail)
                .phoneNumber(customerPhone)
                .paymentGateway("Razorpay")
                .paymentMode("Card")
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}
