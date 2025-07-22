package com.niteshsynergy.payment.razorpay;


import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Scanner;

@RestController
@RequestMapping("/api/webhook")
public class RazorpayWebhookController {

    @PostMapping("/razorpay")
    public void handleRazorpayWebhook(HttpServletRequest request) {
        try (Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A")) {
            String payload = scanner.hasNext() ? scanner.next() : "";
            // Verify and process the payload
            // Refer to Razorpay's webhook verification documentation
        } catch (Exception e) {
            // Handle exception
        }
    }
}
