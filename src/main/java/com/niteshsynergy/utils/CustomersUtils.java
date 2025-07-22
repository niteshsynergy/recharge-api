package com.niteshsynergy.utils;

import java.util.UUID;

public class CustomersUtils {

    // Method to generate a TraceId (UUID will ensure it's unique)
    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    // Method to generate a ParentId (UUID will ensure it's unique)
    public static String generateParentId() {
        return UUID.randomUUID().toString();
    }
}
