package com.niteshsynergy.payment.hdfc;

import com.niteshsynergy.model.PaymentRequest;
import com.niteshsynergy.model.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class HdfcPaymentProcess {

//    @Value("${hdfc.api.key}")
//    private String apiKey;
//
//    @Value("${hdfc.api.endpoint}")
//    private String paymentUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public PaymentResponse processPayment(PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + apiKey);
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);
//
//            ResponseEntity<PaymentResponse> apiResponse = restTemplate.exchange(paymentUrl, HttpMethod.POST, entity, PaymentResponse.class);
//
//            response = apiResponse.getBody();
//        } catch (Exception e) {
//            response.setStatus("FAILED");
//            response.setMessage("HDFC Payment Error: " + e.getMessage());
//        }
        return response;
    }
}
