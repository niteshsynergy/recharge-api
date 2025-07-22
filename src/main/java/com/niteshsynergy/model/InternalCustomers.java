package com.niteshsynergy.model;

import com.niteshsynergy.payment.upi.ValidUpiId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "internal_customers") // Parent Collection
public class InternalCustomers {
    @Id
    private String id; // MongoDB auto-generates ObjectId

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @NotNull(message = "Phone number is required")
    @Digits(integer = 10, fraction = 0, message = "Phone number must be 10 digits")
    private Long phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @PositiveOrZero(message = "Amount cannot be negative")
    private Double amount; // Optional when save-customer api call. This value will come in

    @NotBlank(message = "Client Name is required")
    private String clientName; // Must be "Individual" | Individual   | For Internal Like  Apple, Sapient

    @NotBlank(message = "Channel Type is required")
    private String channelType; // Must be "Individual"  | Individual |  For Internal Like Apple(Iphone, Macbook), Sapient(E1,E2)

    @NotBlank(message = "Created By field is required")
    private String createdBy; // "Admin" or "User" or "Staff"


    // System-generated fields (Should NOT be provided by API caller)
    private String traceId;
    private String parentId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private int status=1; // Default active (can be updated by admin)

    private int isPremium;

}
