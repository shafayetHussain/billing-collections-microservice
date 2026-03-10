package com.example.billingcollections.service;

import com.example.billingcollections.entity.Policy;
import com.example.billingcollections.entity.PremiumSchedule;
import com.example.billingcollections.enums.PolicyStatus;
import com.example.billingcollections.enums.ScheduleStatus;
import com.example.billingcollections.exception.ResourceNotFoundException;
import com.example.billingcollections.repository.PolicyRepository;
import com.example.billingcollections.repository.PremiumScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// Tests for PolicyService focusing on the premium schedule retrieval flow —
// both when the policy exists and when it doesn't
@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PremiumScheduleRepository premiumScheduleRepository;

    // Mocks above get wired into the service automatically via @InjectMocks
    @InjectMocks
    private PolicyService policyService;

    @Test
    void shouldReturnPremiumSchedule() {
        // A basic active policy — status matters here since the service likely
        // gates certain operations on it
        Policy policy = Policy.builder()
                .policyId("POL123")
                .customerId("CUST1001")
                .status(PolicyStatus.ACTIVE)
                .build();

        // One installment that's already been paid — keeps the test data simple
        // but still realistic enough to catch mapping issues
        PremiumSchedule schedule = PremiumSchedule.builder()
                .id(1L)
                .policyId("POL123")
                .installmentNo(1)
                .dueDate(LocalDate.of(2026, 1, 10))
                .amountDue(new BigDecimal("150.00"))
                .status(ScheduleStatus.PAID)
                .build();

        when(policyRepository.findById("POL123")).thenReturn(Optional.of(policy));

        // Using the ordered query here since installment ordering is important
        // for how schedules are displayed to the customer
        when(premiumScheduleRepository.findByPolicyIdOrderByInstallmentNoAsc("POL123"))
                .thenReturn(List.of(schedule));

        var response = policyService.getPremiumSchedule("POL123");

        // Verifying the response is properly assembled — policy ID should
        // flow through and the installment list shouldn't get lost in mapping
        assertNotNull(response);
        assertEquals("POL123", response.getPolicyId());
        assertEquals(1, response.getInstallments().size());
    }

    @Test
    void shouldThrowWhenPolicyNotFound() {
        // POL999 is intentionally a non-existent ID — the service shouldn't
        // silently return an empty response, it should fail loudly
        when(policyRepository.findById("POL999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> policyService.getPremiumSchedule("POL999"));
    }
}