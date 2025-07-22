package com.niteshsynergy.service;

import com.niteshsynergy.model.SubscriptionPlan;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubscriptionPlanService {

    CompletableFuture<SubscriptionPlan> createSubscription(SubscriptionPlan subscriptionPlan);

    CompletableFuture<List<SubscriptionPlan>> getPlanStatusByPhone(String phone);
}
