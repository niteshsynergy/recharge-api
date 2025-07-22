//package com.niteshsynergy.payment.razorpay;
//
//import com.niteshsynergy.model.PaymentRequest;
//import com.niteshsynergy.model.PaymentResponse;
//import com.razorpay.*;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RazorpayPaymentProcessor {
//
//    private RazorpayClient razorpayClient;
//
//    public RazorpayPaymentProcessor(@Value("${razorpay.key_id}") String keyId,
//                                    @Value("${razorpay.key_secret}") String keySecret) throws RazorpayException {
//        this.razorpayClient = new RazorpayClient(keyId, keySecret);
//    }
//
//    public PaymentResponse processPayment(PaymentRequest request) {
//        PaymentResponse response = new PaymentResponse();
////        try {
////            JSONObject options = new JSONObject();
////            options.put("amount", request.getAmount() * 100); // Amount in paise
////            options.put("currency", request.getCurrency());
////            options.put("receipt", request.getOrderId());
////
////            Order order = razorpayClient.Orders.create(options);
////
////            response.setTransactionId(order.get("id"));
////            response.setStatus("SUCCESS");
////            response.setMessage("Payment order created successfully");
////
////        } catch (RazorpayException e) {
////            response.setStatus("FAILED");
////            response.setMessage("Razorpay Payment Error: " + e.getMessage());
////        }
//        return response;
//    }
//}
