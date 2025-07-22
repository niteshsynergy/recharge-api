package com.niteshsynergy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "subscription_plan")
public class SubscriptionPlan {

    @Id
    private String id;
    private String customerId;
    private String phone;
    private String clientName;
    private String planId;
    private String channelType;
    private String parentId;
    private String transactionId;
    private String traceId;
    private Double amount;
    private String status; // "1" for active, "0" for inactive
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private LocalDateTime expiryDate;
}
