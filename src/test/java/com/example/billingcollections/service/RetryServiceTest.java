package com.example.billingcollections.service;

import com.example.billingcollections.entity.PaymentAttempt;
import com.example.billingcollections.entity.RetryTask;
import com.example.billingcollections.enums.PaymentResult;
import com.example.billingcollections.exception.InvalidOperationException;
import com.example.billingcollections.exception.ResourceNotFoundException;
import com.example.billingcollections.repository.PaymentAttemptRepository;
import com.example.billingcollections.repository.RetryTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Unit tests for RetryService — covers the main retry trigger scenarios
// including the happy path and the two common failure modes
@ExtendWith(MockitoExtension.class)
class RetryServiceTest {

    @Mock
    private PaymentAttemptRepository paymentAttemptRepository;

    @Mock
    private RetryTaskRepository retryTaskRepository;

    // RetryService gets its mocked repos injected automatically
    @InjectMocks
    private RetryService retryService;

    @Test
    void shouldTriggerRetry() {
        // Set up a realistic failed attempt that's still eligible for retry
        PaymentAttempt attempt = PaymentAttempt.builder()
                .attemptId("ATT-1001")
                .policyId("POL123")
                .result(PaymentResult.FAILURE)
                .retryEligible(true)
                .recordedAt(OffsetDateTime.now())
                .build();

        when(paymentAttemptRepository.findById("ATT-1001"))
                .thenReturn(Optional.of(attempt));

        // Just return whatever RetryTask gets passed in — we don't need DB persistence here
        when(retryTaskRepository.save(any(RetryTask.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = retryService.triggerRetry("ATT-1001");

        // The response should tie back to the original attempt and carry a fresh retry ID
        assertEquals("ATT-1001", response.getOriginalAttemptId());
        assertNotNull(response.getRetryAttemptId());
    }

    @Test
    void shouldThrowWhenAttemptNotFound() {
        // Simulating a lookup for an attempt ID that doesn't exist in the system
        when(paymentAttemptRepository.findById("ATT-9999"))
                .thenReturn(Optional.empty());

        // Expecting the service to bubble this up as a not-found exception
        assertThrows(ResourceNotFoundException.class,
                () -> retryService.triggerRetry("ATT-9999"));
    }

    @Test
    void shouldThrowWhenRetryNotAllowed() {
        // This attempt exists but has already been marked ineligible for retry
        // (e.g. max retries hit, or a non-retriable failure reason)
        PaymentAttempt attempt = PaymentAttempt.builder()
                .attemptId("ATT-1002")
                .retryEligible(false)
                .build();

        when(paymentAttemptRepository.findById("ATT-1002"))
                .thenReturn(Optional.of(attempt));

        // Service should reject this cleanly rather than silently scheduling another retry
        assertThrows(InvalidOperationException.class,
                () -> retryService.triggerRetry("ATT-1002"));
    }
}