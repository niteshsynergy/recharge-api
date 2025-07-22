package com.niteshsynergy.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageError {
    private String id;
    private String traceId;
    private String transactionId;
    private String parentId;
    private String errorCode;
    private String errorMessage;

    public String getMessage() {
        return errorMessage;
    }
}
