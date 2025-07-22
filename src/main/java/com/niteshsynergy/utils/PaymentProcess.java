package com.niteshsynergy.utils;

import com.niteshsynergy.constant.RazorpayPaymentCred;
import com.niteshsynergy.model.PaymentRequest;
import com.niteshsynergy.model.PaymentResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Refund;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.niteshsynergy.constant.PaymentConstant.*;
import static com.niteshsynergy.constant.RazorPayConstant.RAZORPAY;
import static com.niteshsynergy.payment.upi.UpiProvider.isValidUpiId;

@Service
public class PaymentProcess {

    private static final Logger logger = LoggerFactory.getLogger(PaymentProcess.class);

    @Autowired
    private RazorpayPaymentCred razorpayPaymentCred;

    public PaymentResponse initiatePayment(PaymentRequest request, String paymentGateway) {
        PaymentResponse response = new PaymentResponse();

        try {
            if (!RAZORPAY.equalsIgnoreCase(paymentGateway)) {
                response.setStatus(STATUS_FAILED);
                response.setMessage("Invalid payment gateway: " + paymentGateway);
                return response;
            }

            if (razorpayPaymentCred == null || razorpayPaymentCred.getKeyId() == null || razorpayPaymentCred.getKeySecret() == null) {
                throw new Exception("Razorpay credentials are missing.");
            }

            // ✅ Validate UPI ID format
            if (!isValidUpiId(request.getUpiId())) {
                response.setStatus(STATUS_FAILED);
                response.setMessage("Invalid UPI ID format.");
                return response;
            }

            // ✅ Initialize Razorpay Client
            RazorpayClient razorpayClient = new RazorpayClient(
                    razorpayPaymentCred.getKeyId(),
                    razorpayPaymentCred.getKeySecret()
            );

            // ✅ Create Razorpay Order
            Order order = createRazorpayUpiOrder(razorpayClient, request);
            if (order == null || order.get("id") == null) {
                throw new RuntimeException("Failed to create Razorpay order.");
            }

            String orderId = order.get("id").toString();
            String transactionId = request.getTransactionId();

            // ✅ Handle Razorpay Status Before Polling
            String initialStatus = order.has("status") ? order.get("status").toString() : STATUS_PENDING;

            if ("paid".equalsIgnoreCase(initialStatus) || "captured".equalsIgnoreCase(initialStatus)) {
                response.setStatus(STATUS_SUCCESS);
                response.setMessage("Payment processed successfully with Razorpay.");
                response.setTransactionId(transactionId);
                response.setOrderId(orderId);
                return response;
            }

            response.setTransactionId(transactionId);
            response.setOrderId(orderId);
            response.setStatus(STATUS_PENDING);
            response.setMessage("Payment initiated, waiting for status update.");

            // ✅ NEW: Poll for Payment Status (Waits up to 3 minutes)
            String finalPaymentStatus = pollPaymentStatus(razorpayClient, orderId, 180);

            switch (finalPaymentStatus) {
                case "captured":
                case "paid":
                    response.setStatus(STATUS_SUCCESS);
                    response.setMessage("Payment processed successfully.");
                    break;

                case "failed":
                    response.setStatus(STATUS_FAILED);
                    response.setMessage("Payment failed.");
                    break;

                case "timeout":
                case "pending":
                case "authorized":
                    // ✅ If payment still pending after 3 minutes, trigger refund
                    boolean refundStatus = initiateRefund(razorpayClient, orderId);
                    if (refundStatus) {
                        response.setStatus(STATUS_REFUNDED);
                        response.setMessage("Payment refunded due to timeout or technical issue.");
                    } else {
                        response.setStatus(STATUS_FAILED);
                        response.setMessage("Payment timeout or technical issue, but refund failed.");
                    }
                    break;

                default:
                    response.setStatus(STATUS_UNKNOWN);
                    response.setMessage("Payment status unknown.");
                    break;
            }

        } catch (Exception e) {
            logger.error("Payment initiation failed: {}", e.getMessage());
            response.setStatus(STATUS_FAILED);
            response.setMessage("Payment failed due to an internal error.");
        }

        return response;
    }

    private boolean initiateRefund(RazorpayClient razorpayClient, String orderId) {
        try {
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", 100); // Refund full amount (amount in paise)
            refundRequest.put("speed", "normal");

            Refund refund = razorpayClient.Payments.refund(orderId, refundRequest);
            return refund != null && refund.get("status").toString().equalsIgnoreCase("processed");
        } catch (Exception e) {
            logger.error("Refund failed for order {}: {}", orderId, e.getMessage());
            return false;
        }
    }

    private String pollPaymentStatus(RazorpayClient razorpayClient, String orderId, int maxWaitSeconds) {
        int waitTime = 0;
        int interval = 10; // Check every 10 seconds

        while (waitTime < maxWaitSeconds) {
            try {
                Order order = razorpayClient.Orders.fetch(orderId);
                String status = order.get("status").toString();

                if ("paid".equalsIgnoreCase(status) || "captured".equalsIgnoreCase(status)) {
                    return "paid";
                } else if ("failed".equalsIgnoreCase(status)) {
                    return "failed";
                } else if ("pending".equalsIgnoreCase(status) || "authorized".equalsIgnoreCase(status)) {
                    Thread.sleep(interval * 1000); // Wait for next check
                    waitTime += interval;
                } else {
                    return "unknown";
                }
            } catch (Exception e) {
                return "error";
            }
        }

        return "timeout"; // If still pending after max wait time
    }

    private Order createRazorpayUpiOrder(RazorpayClient razorpayClient, PaymentRequest request) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount() * 100);  // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);  // Auto capture payment

            // ✅ Enable UPI as a payment method
            orderRequest.put("method", "upi");

            // ✅ Add UPI ID (VPA) in the request
            JSONObject upiRequest = new JSONObject();
            upiRequest.put("vpa", request.getUpiId()); // Example: "spring@upi"
            orderRequest.put("upi", upiRequest);

            return razorpayClient.Orders.create(orderRequest);
        } catch (Exception e) {
            throw new RuntimeException("Razorpay UPI order creation failed: " + e.getMessage(), e);
        }
    }
}