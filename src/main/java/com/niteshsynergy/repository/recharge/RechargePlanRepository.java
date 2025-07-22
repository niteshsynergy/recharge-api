package com.niteshsynergy.repository.recharge;

import com.niteshsynergy.entity.RechargePlan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RechargePlanRepository extends MongoRepository<RechargePlan, String> {
}
