package com.niteshsynergy.exception.individual;

import com.niteshsynergy.exception.MessageError;
import org.springframework.stereotype.Component;

@Component
public class IndividualCustomerException {

    // Method 1: Save Customer Exception
    public MessageError saveIndividualCustomerError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "customer_save_error",    // Custom ID
                traceId,
                transactionId,
                parentId,
                "401",                    // Example error code
                "Failed to save customer: " + errorMessage
        );
    }

    // Method 2: Process Payment Exception
    public MessageError processIndividualPaymentError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "payment_process_error",    // Custom ID
                traceId,
                transactionId,
                parentId,
                "402",                      // Example error code
                "Failed to process payment: " + errorMessage
        );
    }

    // Method 3: Save Transaction Exception
    public MessageError saveIndividualTransactionWithRetryError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "transaction_save_error",    // Custom ID
                traceId,
                transactionId,
                parentId,
                "404",                        // Example error code
                "Failed to save transaction: " + errorMessage
        );
    }

    // Method 4: Final Transaction Exception
    public MessageError saveFinalTransactionError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "final_transaction_save_error",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "405",                             // Example error code
                "Failed to save final transaction: " + errorMessage
        );
    }

    // Method 5: Rollback Customer Exception
    public MessageError rollbackCustomerError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "customer_rollback_error",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "406",                        // Example error code
                "Failed to rollback customer: " + errorMessage
        );
    }

    // Method 6: Rollback Payment Exception
    public MessageError rollbackPaymentError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "payment_rollback_error",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "407",                     // Example error code
                "Failed to rollback payment: " + errorMessage
        );
    }

    // Method 7: General Error Handling
    public MessageError generalError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "general_error",         // Custom ID
                traceId,
                transactionId,
                parentId,
                "500",                   // Example error code
                "An unexpected error occurred: " + errorMessage
        );
    }

    // Method 8: Transaction Failure after Rollback
    public MessageError transactionFailureError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "transaction_failure_after_rollback",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "505",                           // Example error code
                "Transaction failed after rollback: " + errorMessage
        );
    }
}
