package com.niteshsynergy.utils.individual;

import com.niteshsynergy.model.IndividualCustomers;
import java.time.LocalDateTime;

import static com.niteshsynergy.utils.CustomersUtils.generateParentId;
import static com.niteshsynergy.utils.CustomersUtils.generateTraceId;

public class PrepareIndividualCustomersData {

    public static IndividualCustomers prepareIndividualCustomersData(IndividualCustomers customer) {
        // Setting the system-generated fields
        customer.setTraceId(generateTraceId()); // Set TraceId
        customer.setParentId(generateParentId()); // Set ParentId
        customer.setCreatedOn(LocalDateTime.now()); // Set CreatedOn to current time
        customer.setUpdatedOn(LocalDateTime.now()); // Set UpdatedOn to current time
        // Assuming `amount` needs to be updated based on business logic
        customer.setAmount(0.0);  // Set amount to 0.0 as it's part of child documents

        return customer;
    }
}
