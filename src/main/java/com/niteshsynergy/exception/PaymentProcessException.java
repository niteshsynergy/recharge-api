package com.niteshsynergy.exception;

// PaymentProcessException.java
public class PaymentProcessException extends RuntimeException {
    private String traceId;
    private String transactionId;
    private String parentId;

    public PaymentProcessException(String traceId, String transactionId, String parentId, String message) {
        super(message);
        this.traceId = traceId;
        this.transactionId = transactionId;
        this.parentId = parentId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getParentId() {
        return parentId;
    }
}


