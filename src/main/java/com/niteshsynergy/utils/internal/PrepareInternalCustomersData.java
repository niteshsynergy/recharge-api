package com.niteshsynergy.utils.internal;

import com.niteshsynergy.model.InternalCustomers;
import java.time.LocalDateTime;

import static com.niteshsynergy.utils.CustomersUtils.generateParentId;
import static com.niteshsynergy.utils.CustomersUtils.generateTraceId;

public class PrepareInternalCustomersData {

    public static InternalCustomers prepareInternalCustomersData(InternalCustomers customer) {
        // Setting the system-generated fields
        customer.setTraceId(generateTraceId()); // Set TraceId
        customer.setParentId(generateParentId()); // Set ParentId
        customer.setCreatedOn(LocalDateTime.now()); // Set CreatedOn to current time
        customer.setUpdatedOn(LocalDateTime.now()); // Set UpdatedOn to current time

        // Set createdBy to "Admin" for internal customers as per your logic
        customer.setCreatedBy("Admin");

        // Assuming `amount` needs to be updated based on business logic
        customer.setAmount(0.0);  // Set initial amount to 0.0

        return customer;
    }
}
