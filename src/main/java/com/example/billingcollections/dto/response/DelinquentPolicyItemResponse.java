package com.example.billingcollections.dto.response;

import com.example.billingcollections.enums.DelinquencyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelinquentPolicyItemResponse {

    private String policyId;
    private Integer daysPastDue;
    private DelinquencyStatus delinquencyStatus;
}