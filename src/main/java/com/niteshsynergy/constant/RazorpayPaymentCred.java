package com.niteshsynergy.constant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor  // Allow default constructor
public final class RazorpayPaymentCred implements Serializable {

    private static final long serialVersionUID = 1L;

    // Immutable fields (final fields)
    private String merchantId;
    private String keyId;
    private String keySecret;
    private String webhookSecret;
    private String publicKey;
    private String apiEndpointUrl;

    // Getters for each field (no setters to ensure immutability)

    public String getMerchantId() {
        return merchantId;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getApiEndpointUrl() {
        return apiEndpointUrl;
    }
}
