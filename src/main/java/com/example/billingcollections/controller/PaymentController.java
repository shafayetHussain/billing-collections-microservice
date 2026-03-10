package com.example.billingcollections.controller;

import com.example.billingcollections.dto.request.PaymentAttemptRequest;
import com.example.billingcollections.dto.response.PaymentAttemptResponse;
import com.example.billingcollections.dto.response.RetryResponse;
import com.example.billingcollections.service.PaymentService;
import com.example.billingcollections.service.RetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for payment-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final RetryService retryService;

    @PostMapping("/attempts")
    public ResponseEntity<PaymentAttemptResponse> recordPaymentAttempt(
            @Valid @RequestBody PaymentAttemptRequest request) {
        log.info("Received request to record payment attempt for policyId={}", request.getPolicyId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.recordPaymentAttempt(request));
    }

    @PostMapping("/attempts/{attemptId}/retry")
    public ResponseEntity<RetryResponse> triggerRetry(@PathVariable String attemptId) {
        log.info("Received request to trigger retry for attemptId={}", attemptId);
        return ResponseEntity.ok(retryService.triggerRetry(attemptId));
    }
}