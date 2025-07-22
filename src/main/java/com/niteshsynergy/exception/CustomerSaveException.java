package com.niteshsynergy.exception;

public class CustomerSaveException extends RuntimeException {
    private String traceId;
    private String transactionId;
    private String parentId;

    // Constructor with 4 parameters - saveFinalTransaction save child data only.
    public CustomerSaveException(String traceId, String transactionId, String parentId, String message) {
        super(message);
        this.traceId = traceId;
        this.transactionId = transactionId;
        this.parentId = parentId;
    }

    // Constructor with 3 parameters - saveIndividualCustomer save parent data only.
    public CustomerSaveException(String traceId, String parentId, String message) {
        super(message);
        this.traceId = traceId;
        this.parentId = parentId;
        this.transactionId = "Individual Customer Dont Have TransactionId";
    }

    public String getTraceId() { return traceId; }
    public String getTransactionId() { return transactionId; }
    public String getParentId() { return parentId; }
}
