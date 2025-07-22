package com.niteshsynergy.repository.recharge;


import com.niteshsynergy.model.SubscriptionPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, String> {
    List<SubscriptionPlan> findByPhone(String phone);
    List<SubscriptionPlan> findByCustomerId(String customerId);
}
