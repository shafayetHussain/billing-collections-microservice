package com.example.billingcollections.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.billingcollections.dto.response.DelinquentPoliciesResponse;
import com.example.billingcollections.dto.response.PremiumScheduleResponse;
import com.example.billingcollections.service.DelinquencyService;
import com.example.billingcollections.service.PolicyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/policies")
@RequiredArgsConstructor
public class PolicyController {
	
	private final PolicyService policyService;
	private final DelinquencyService delinquencyService;
	
	@GetMapping("/{policyId}/premium-schedule")
	public ResponseEntity<PremiumScheduleResponse> getPremiumSchedule(@PathVariable String policyId){
		log.info("Received request to fetch premium schedule for policyId={}", policyId);
        return ResponseEntity.ok(policyService.getPremiumSchedule(policyId));
	}
	
	 @GetMapping("/delinquent")
	    public ResponseEntity<DelinquentPoliciesResponse> getDelinquentPolicies(
	            @RequestParam(defaultValue = "1") Integer daysPastDue) {
	        log.info("Received request to fetch delinquent policies for daysPastDue={}", daysPastDue);
	        return ResponseEntity.ok(delinquencyService.getDelinquentPolicies(daysPastDue));
	    }
	

}
