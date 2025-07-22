package com.niteshsynergy.controller;
import com.niteshsynergy.entity.RechargePlan;
import com.niteshsynergy.service.RechargePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recharge-plans")
@RequiredArgsConstructor
public class RechargePlanController {

    private final RechargePlanService rechargePlanService;

    // Save or Update Recharge Plan
    @PostMapping("/save")
    public ResponseEntity<RechargePlan> saveRechargePlan(@RequestBody RechargePlan rechargePlan) {
        RechargePlan savedPlan = rechargePlanService.saveRechargePlan(rechargePlan);
        return new ResponseEntity<>(savedPlan, HttpStatus.CREATED);
    }

    // Get All Recharge Plans
    @GetMapping("/all")
    public ResponseEntity<List<RechargePlan>> getAllRechargePlans() {
        List<RechargePlan> plans = rechargePlanService.getAllRechargePlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    // Get Recharge Plan by Client Name
    @GetMapping("/client/{name}")
    public ResponseEntity<RechargePlan> getByClientName(@PathVariable String name) {
        RechargePlan plan = rechargePlanService.getByClientName(name);
        return plan != null
                ? new ResponseEntity<>(plan, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get Plans by Channel Type
    @GetMapping("/channel/{type}")
    public ResponseEntity<List<RechargePlan.Plan>> getByChannelType(@PathVariable String type) {
        List<RechargePlan.Plan> plans = rechargePlanService.getPlansByChannelType(type);
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }
}
