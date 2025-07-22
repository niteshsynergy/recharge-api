package com.niteshsynergy.payment.upi;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpiIdValidator implements ConstraintValidator<ValidUpiId, String> {
    @Override
    public boolean isValid(String upiId, ConstraintValidatorContext context) {
        return UpiProvider.isValidUpi(upiId);
    }
}
