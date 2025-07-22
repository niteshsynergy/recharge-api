package com.niteshsynergy.exception.internal;

import com.niteshsynergy.exception.MessageError;
import org.springframework.stereotype.Component;

@Component
public class InternalCustomerException {

    // Method 1: Save Customer Exception
    public MessageError saveInternalCustomerError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_customer_save_error",    // Custom ID
                traceId,
                transactionId,
                parentId,
                "411",                    // Example error code
                "Failed to save internal customer: " + errorMessage
        );
    }

    // Method 2: Process Payment Exception
    public MessageError processInternalPaymentError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_payment_process_error",    // Custom ID
                traceId,
                transactionId,
                parentId,
                "412",                      // Example error code
                "Failed to process internal payment: " + errorMessage
        );
    }

    // Method 3: Save Transaction Exception
    public MessageError saveInternalTransactionWithRetryError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_transaction_save_error",    // Custom ID
                traceId,
                transactionId,
                parentId,
                "414",                        // Example error code
                "Failed to save internal transaction: " + errorMessage
        );
    }

    // Method 4: Final Transaction Exception
    public MessageError saveFinalInternalTransactionError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "final_internal_transaction_save_error",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "415",                             // Example error code
                "Failed to save final internal transaction: " + errorMessage
        );
    }

    // Method 5: Rollback Customer Exception
    public MessageError rollbackInternalCustomerError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_customer_rollback_error",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "416",                        // Example error code
                "Failed to rollback internal customer: " + errorMessage
        );
    }

    // Method 6: Rollback Payment Exception
    public MessageError rollbackInternalPaymentError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_payment_rollback_error",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "417",                     // Example error code
                "Failed to rollback internal payment: " + errorMessage
        );
    }

    // Method 7: General Error Handling
    public MessageError generalInternalError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_general_error",         // Custom ID
                traceId,
                transactionId,
                parentId,
                "500",                   // Example error code
                "An unexpected internal error occurred: " + errorMessage
        );
    }

    // Method 8: Transaction Failure after Rollback
    public MessageError transactionFailureInternalError(String traceId, String transactionId, String parentId, String errorMessage) {
        return new MessageError(
                "internal_transaction_failure_after_rollback",  // Custom ID
                traceId,
                transactionId,
                parentId,
                "505",                           // Example error code
                "Transaction failed after rollback: " + errorMessage
        );
    }
}
