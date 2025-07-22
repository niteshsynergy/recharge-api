package com.niteshsynergy.service.impl.recharge;

import com.niteshsynergy.model.SubscriptionPlan;
import com.niteshsynergy.repository.recharge.SubscriptionPlanRepository;
import com.niteshsynergy.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public CompletableFuture<SubscriptionPlan> createSubscription(SubscriptionPlan subscriptionPlan) {
        // Generate Trace ID and Transaction ID
        subscriptionPlan.setTransactionId(UUID.randomUUID().toString());
        subscriptionPlan.setTraceId(UUID.randomUUID().toString());
        subscriptionPlan.setCreatedOn(LocalDateTime.now());
        subscriptionPlan.setUpdatedOn(LocalDateTime.now());
        subscriptionPlan.setStatus("1"); // Active by default

        // Save subscription plan asynchronously
        return CompletableFuture.supplyAsync(() -> subscriptionPlanRepository.save(subscriptionPlan));
    }

    @Override
    public CompletableFuture<List<SubscriptionPlan>> getPlanStatusByPhone(String phone) {
        return CompletableFuture.supplyAsync(() -> subscriptionPlanRepository.findByPhone(phone));
    }
}
