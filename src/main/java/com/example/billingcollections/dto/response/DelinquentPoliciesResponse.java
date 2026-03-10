package com.example.billingcollections.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelinquentPoliciesResponse {

    private Integer count;
    private List<DelinquentPolicyItemResponse> policies;
}