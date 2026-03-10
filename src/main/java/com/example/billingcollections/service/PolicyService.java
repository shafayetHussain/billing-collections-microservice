package com.example.billingcollections.service;

import com.example.billingcollections.dto.response.PremiumScheduleItemResponse;
import com.example.billingcollections.dto.response.PremiumScheduleResponse;
import com.example.billingcollections.entity.Policy;
import com.example.billingcollections.entity.PremiumSchedule;
import com.example.billingcollections.exception.ResourceNotFoundException;
import com.example.billingcollections.repository.PolicyRepository;
import com.example.billingcollections.repository.PremiumScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles policy-related business operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final PremiumScheduleRepository premiumScheduleRepository;

    public PremiumScheduleResponse getPremiumSchedule(String policyId) {
        log.info("Fetching premium schedule for policyId={}", policyId);
        //Fetching policy
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + policyId));
        
        // all the schedule
        List<PremiumSchedule> schedules = premiumScheduleRepository.findByPolicyIdOrderByInstallmentNoAsc(policyId);

        //item schedule
        List<PremiumScheduleItemResponse> items = schedules.stream()
                .map(schedule -> PremiumScheduleItemResponse.builder()
                        .installmentNo(schedule.getInstallmentNo())
                        .dueDate(schedule.getDueDate())
                        .amountDue(schedule.getAmountDue())
                        .status(schedule.getStatus())
                        .build())
                .toList();

        return PremiumScheduleResponse.builder()
                .policyId(policy.getPolicyId())
                .currency("USD")
                .installments(items)
                .build();
    }
}