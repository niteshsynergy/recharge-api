package com.niteshsynergy.payment.hdfc;


import com.niteshsynergy.model.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class HdfcRefundService {

//    @Value("${hdfc.api.url}")
//    private String hdfcApiUrl;

//    private final RestTemplate restTemplate = new RestTemplate();

    public PaymentResponse processRefund(String transactionId, Double amount) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        RefundRequest refundRequest = new RefundRequest(transactionId, amount);
//        HttpEntity<RefundRequest> entity = new HttpEntity<>(refundRequest, headers);
//
//        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
//                hdfcApiUrl + "/refund",
//                HttpMethod.POST,
//                entity,
//                PaymentResponse.class
//        );
//
//        return response.getBody();
        return new PaymentResponse();
    }
}

