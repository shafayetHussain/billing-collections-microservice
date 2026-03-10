package com.example.billingcollections.dto.response;

import com.example.billingcollections.enums.PaymentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAttemptResponse {

    private String attemptId;
    private String policyId;
    private PaymentResult result;
    private Boolean retryEligible;
    private String transactionId;
    private OffsetDateTime recordedAt;
}