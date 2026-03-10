package com.example.billingcollections.service;

import com.example.billingcollections.dto.response.DelinquentPoliciesResponse;
import com.example.billingcollections.dto.response.DelinquentPolicyItemResponse;
import com.example.billingcollections.entity.DelinquencyRecord;
import com.example.billingcollections.repository.DelinquencyRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles delinquency-related business operations. 
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DelinquencyService {

    private final DelinquencyRecordRepository delinquencyRecordRepository;

    public DelinquentPoliciesResponse getDelinquentPolicies(Integer daysPastDue) {
        log.info("Fetching delinquent policies for daysPastDue >= {}", daysPastDue);
        
        
        //all record
        List<DelinquencyRecord> records =
                delinquencyRecordRepository.findByDaysPastDueGreaterThanEqual(daysPastDue);

        List<DelinquentPolicyItemResponse> policies = records.stream()
                .map(record -> DelinquentPolicyItemResponse.builder()
                        .policyId(record.getPolicyId())
                        .daysPastDue(record.getDaysPastDue())
                        .delinquencyStatus(record.getDelinquencyStatus())
                        .build())
                .toList();

        return DelinquentPoliciesResponse.builder()
                .count(policies.size())
                .policies(policies)
                .build();
    }
}