package com.niteshsynergy.payment.upi;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UpiIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpiId {
    String message() default "Invalid UPI ID format. Must match a valid bank or provider UPI suffix.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
