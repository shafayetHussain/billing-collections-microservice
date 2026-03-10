package com.example.billingcollections.service;

import com.example.billingcollections.client.PaymentGatewayClient;
import com.example.billingcollections.dto.request.PaymentAttemptRequest;
import com.example.billingcollections.dto.response.PaymentAttemptResponse;
import com.example.billingcollections.dto.response.PaymentGatewayResponse;
import com.example.billingcollections.entity.PaymentAttempt;
import com.example.billingcollections.enums.PaymentResult;
import com.example.billingcollections.repository.PaymentAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Handles payment attempt recording and idempotency behavior.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentAttemptRepository paymentAttemptRepository;
    private final PaymentGatewayClient paymentGatewayClient;

    /**
     * Checks whether a payment request with the same idempotency key was already processed.
     * If yes, returns the existing response. Otherwise, creates a new payment attempt.
     */
    public PaymentAttemptResponse recordPaymentAttempt(PaymentAttemptRequest request) {
        log.info("Recording payment attempt for policyId={}, installmentNo={}, idempotencyKey={}",
                request.getPolicyId(), request.getInstallmentNo(), request.getIdempotencyKey());

        return paymentAttemptRepository.findByIdempotencyKey(request.getIdempotencyKey())
                .map(existing -> {
                    log.info("Duplicate idempotency key detected, returning existing payment attempt: {}",
                            existing.getAttemptId());

                    return PaymentAttemptResponse.builder()
                            .attemptId(existing.getAttemptId())
                            .policyId(existing.getPolicyId())
                            .result(existing.getResult())
                            .retryEligible(existing.getRetryEligible())
                            .transactionId(existing.getExternalTransactionId())
                            .recordedAt(existing.getRecordedAt())
                            .build();
                })
                .orElseGet(() -> createNewPaymentAttempt(request));
    }

    /**
     * Creates and persists a new payment attempt.
     * Calls the payment gateway, evaluates retry eligibility for failed payments,
     * and returns the saved payment attempt response.
     */
    private PaymentAttemptResponse createNewPaymentAttempt(PaymentAttemptRequest request) {
        PaymentGatewayResponse gatewayResponse = paymentGatewayClient.processPayment(request);

        boolean retryEligible = request.getResult() == PaymentResult.FAILURE
                && request.getFailureReason() != null
                && paymentGatewayClient.isRetryEligible(request.getFailureReason());

        log.info("Failure evaluation: result={}, failureReason={}, retryEligible={}",
                request.getResult(), request.getFailureReason(), retryEligible);

        PaymentAttempt paymentAttempt = PaymentAttempt.builder()
                .attemptId("ATT-" + UUID.randomUUID())
                .policyId(request.getPolicyId())
                .installmentNo(request.getInstallmentNo())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .result(request.getResult())
                .failureReason(request.getFailureReason())
                .idempotencyKey(request.getIdempotencyKey())
                .externalTransactionId(gatewayResponse.getTransactionId())
                .recordedAt(OffsetDateTime.now())
                .retryEligible(retryEligible)
                .build();

        PaymentAttempt saved = paymentAttemptRepository.save(paymentAttempt);

        log.info("Payment attempt saved successfully: attemptId={}, result={}, retryEligible={}",
                saved.getAttemptId(), saved.getResult(), saved.getRetryEligible());

        return PaymentAttemptResponse.builder()
                .attemptId(saved.getAttemptId())
                .policyId(saved.getPolicyId())
                .result(saved.getResult())
                .retryEligible(saved.getRetryEligible())
                .transactionId(saved.getExternalTransactionId())
                .recordedAt(saved.getRecordedAt())
                .build();
    }
}