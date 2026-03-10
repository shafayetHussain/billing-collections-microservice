package com.example.billingcollections.service;

import com.example.billingcollections.client.PaymentGatewayClient;
import com.example.billingcollections.dto.request.PaymentAttemptRequest;
import com.example.billingcollections.dto.response.PaymentGatewayResponse;
import com.example.billingcollections.entity.PaymentAttempt;
import com.example.billingcollections.enums.FailureReason;
import com.example.billingcollections.enums.PaymentMethod;
import com.example.billingcollections.enums.PaymentResult;
import com.example.billingcollections.repository.PaymentAttemptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// PaymentService tests — the two main paths here are idempotency (don't process
// the same payment twice) and creating a fresh attempt when there's no prior record
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentAttemptRepository paymentAttemptRepository;

    // External gateway — we never want real HTTP calls going out in unit tests
    @Mock
    private PaymentGatewayClient paymentGatewayClient;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldReturnExistingAttemptForSameIdempotencyKey() {
        // Represents an attempt that was already recorded successfully in a previous request
        PaymentAttempt existing = PaymentAttempt.builder()
                .attemptId("ATT-1001")
                .policyId("POL123")
                .result(PaymentResult.SUCCESS)
                .retryEligible(false)   // succeeded, so no retry needed
                .externalTransactionId("txn-123")
                .recordedAt(OffsetDateTime.now())
                .idempotencyKey("idem-1")
                .build();

        // Incoming request carries the same idempotency key — could be a client retry
        // or a duplicate submission, either way we should not charge again
        PaymentAttemptRequest request = PaymentAttemptRequest.builder()
                .policyId("POL123")
                .installmentNo(1)
                .amount(new BigDecimal("150.00"))
                .paymentMethod(PaymentMethod.CARD)
                .result(PaymentResult.SUCCESS)
                .idempotencyKey("idem-1")
                .build();

        when(paymentAttemptRepository.findByIdempotencyKey("idem-1"))
                .thenReturn(Optional.of(existing));

        var response = paymentService.recordPaymentAttempt(request);

        // Should hand back the original attempt, not create a new one
        assertEquals("ATT-1001", response.getAttemptId());
        assertEquals(PaymentResult.SUCCESS, response.getResult());
    }

    @Test
    void shouldCreateNewPaymentAttempt() {
        // Fresh idempotency key — this is a genuinely new payment attempt
        PaymentAttemptRequest request = PaymentAttemptRequest.builder()
                .policyId("POL123")
                .installmentNo(2)
                .amount(new BigDecimal("150.00"))
                .paymentMethod(PaymentMethod.CARD)
                .result(PaymentResult.FAILURE)
                .failureReason(FailureReason.PROCESSOR_TIMEOUT)
                .idempotencyKey("idem-2")
                .build();

        // No existing record for this key, so we proceed with processing
        when(paymentAttemptRepository.findByIdempotencyKey("idem-2"))
                .thenReturn(Optional.empty());

        // Gateway responds with a timeout failure — transactionId is null
        // because the processor never confirmed the transaction
        when(paymentGatewayClient.processPayment(request))
                .thenReturn(PaymentGatewayResponse.builder()
                        .transactionId(null)
                        .status(PaymentResult.FAILURE)
                        .build());

        // PROCESSOR_TIMEOUT is transient, so the gateway considers it retry-eligible
        when(paymentGatewayClient.isRetryEligible(FailureReason.PROCESSOR_TIMEOUT))
                .thenReturn(true);

        // Echo back whatever gets saved — lets us verify the object the service built
        when(paymentAttemptRepository.save(any(PaymentAttempt.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = paymentService.recordPaymentAttempt(request);

        // A new attempt ID should have been generated, result should reflect
        // the gateway response, and retry flag should be set since it's a timeout
        assertNotNull(response.getAttemptId());
        assertEquals(PaymentResult.FAILURE, response.getResult());
        assertTrue(response.getRetryEligible());
    }
}