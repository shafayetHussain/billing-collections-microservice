package com.example.billingcollections.service;

import com.example.billingcollections.dto.response.RetryResponse;
import com.example.billingcollections.entity.PaymentAttempt;
import com.example.billingcollections.entity.RetryTask;
import com.example.billingcollections.enums.RetryStatus;
import com.example.billingcollections.exception.InvalidOperationException;
import com.example.billingcollections.exception.ResourceNotFoundException;
import com.example.billingcollections.repository.PaymentAttemptRepository;
import com.example.billingcollections.repository.RetryTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Handles retry operations for failed payment attempts.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetryService {

    private final PaymentAttemptRepository paymentAttemptRepository;
    private final RetryTaskRepository retryTaskRepository;
    
    
    //Retries a failed paymetn- it looks for original attempt
    public RetryResponse triggerRetry(String attemptId) {
        log.info("Triggering retry for attemptId={}", attemptId);
        
        //fetching the original attempt or throw error
        PaymentAttempt originalAttempt = paymentAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment attempt not found: " + attemptId));
        
        //only failed + elegible attempts can be retried
        if (!Boolean.TRUE.equals(originalAttempt.getRetryEligible())) {
            throw new InvalidOperationException("Retry not allowed for this payment attempt");
        }

        String retryAttemptId = "ATT-" + UUID.randomUUID();
        OffsetDateTime scheduledAt = OffsetDateTime.now().plusMinutes(15);

        RetryTask retryTask = RetryTask.builder()
                .originalAttemptId(originalAttempt.getAttemptId())
                .retryAttemptId(retryAttemptId)
                .retryCount(1)
                .status(RetryStatus.RETRY_TRIGGERED)
                .scheduledAt(scheduledAt)
                .build();

        retryTaskRepository.save(retryTask);

        log.info("Retry scheduled successfully for originalAttemptId={}, retryAttemptId={}",
                attemptId, retryAttemptId);

        return RetryResponse.builder()
                .originalAttemptId(attemptId)
                .retryAttemptId(retryAttemptId)
                .status(RetryStatus.RETRY_TRIGGERED)
                .scheduledAt(scheduledAt)
                .build();
    }
}