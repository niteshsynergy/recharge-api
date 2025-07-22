package com.niteshsynergy.service.impl.recharge;
import com.niteshsynergy.entity.RechargePlan;
import com.niteshsynergy.repository.recharge.RechargePlanRepository;
import com.niteshsynergy.service.RechargePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RechargePlanServiceImpl implements RechargePlanService {

    private final RechargePlanRepository rechargePlanRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public RechargePlan saveRechargePlan(RechargePlan rechargePlan) {
        return rechargePlanRepository.save(rechargePlan);
    }

    @Override
    public List<RechargePlan> getAllRechargePlans() {
        return rechargePlanRepository.findAll();
    }

    @Override
    public RechargePlan getByClientName(String clientName) {
        return rechargePlanRepository
                .findAll()
                .stream()
                .filter(plan -> plan.getClientName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<RechargePlan.Plan> getPlansByChannelType(String channelType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("plans.channelType").is(channelType));

        List<RechargePlan> rechargePlans = mongoTemplate.find(query, RechargePlan.class);

        // Flatten the nested plan list
        return rechargePlans.stream()
                .flatMap(plan -> plan.getPlans().stream())
                .filter(p -> p.getChannelType().equalsIgnoreCase(channelType))
                .collect(Collectors.toList());
    }
}
