package com.niteshsynergy.service;
import com.niteshsynergy.entity.RechargePlan;

import java.util.List;

public interface RechargePlanService {

    // Create or Update RechargePlan
    RechargePlan saveRechargePlan(RechargePlan rechargePlan);

    // Get All RechargePlans
    List<RechargePlan> getAllRechargePlans();

    // Get by ClientName
    RechargePlan getByClientName(String clientName);

    // Get Plans by ChannelType
    List<RechargePlan.Plan> getPlansByChannelType(String channelType);
}
