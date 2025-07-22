package com.niteshsynergy.utils;

import java.util.UUID;

public class OrderTransactionUtils {

    // Method to generate a unique Order ID
    public static String generateOrderId(String clientName, String channelType, String channelMode) {
        // Generate a unique UUID to ensure each order is unique
        String uniqueId = UUID.randomUUID().toString().substring(0, 8); // Shorten UUID for better readability
        // Construct the Order ID using the specified format
        return "OID" + clientName + channelType + channelMode + uniqueId;
    }

    // Method to generate a unique Transaction ID
    public static String generateTransactionId(String clientName, String channelType, String channelMode) {
        // Generate a unique UUID to ensure each transaction is unique
        String uniqueId = UUID.randomUUID().toString().substring(0, 8); // Shorten UUID for better readability
        // Construct the Transaction ID using the specified format
        return "TX" + clientName + channelType + channelMode + uniqueId;
    }
}
