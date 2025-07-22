package com.niteshsynergy.payment.hdfc;

import com.niteshsynergy.model.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hdfc/webhook")
public class HdfcWebhookController {

    @PostMapping("/payment-status")
    public ResponseEntity<String> handlePaymentCallback(@RequestBody PaymentResponse response) {
        if (response.getStatus().equalsIgnoreCase("SUCCESS")) {
            // Update database transaction status to success
            return ResponseEntity.ok("Payment Successful");
        } else {
            // Log and update database as failed
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed");
        }
    }
}
