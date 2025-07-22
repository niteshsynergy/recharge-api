package com.niteshsynergy.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recharge_plans")  // Collection name
public class RechargePlan {

    @Id
    private String id;                      // MongoDB ID
    private String clientName;              // e.g., Mango, Apple
    private List<Plan> plans;               // List of recharge plans

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Plan {
        private String planId;              // Unique Plan ID
        private String productId;           // Associated Product ID
        private String description;         // Plan details
        private String validity;            // Validity period (28 days)
        private Double amount;              // Plan cost
        private int status;                 // 1 = Active, 0 = Inactive
        private String channelType;         // e.g., Ola-BikeService
        private String extraInfo;           // Extra plan info
    }
}
