package com.example.billingcollections.client;

import com.example.billingcollections.dto.request.PaymentAttemptRequest;
import com.example.billingcollections.dto.response.PaymentGatewayResponse;
import com.example.billingcollections.enums.FailureReason;
import com.example.billingcollections.enums.PaymentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Simulates integration with an external payment processor.
 */
@Slf4j
@Component
public class PaymentGatewayClient {

    public PaymentGatewayResponse processPayment(PaymentAttemptRequest request) {
        log.info("Sending payment to third-party processor for policyId={}, installmentNo={}",
                request.getPolicyId(), request.getInstallmentNo());
             //adding payment result if succefullo
        if (request.getResult() == PaymentResult.SUCCESS) {
            String transactionId = "txn-" + UUID.randomUUID();

            log.info("Third-party processor returned SUCCESS for policyId={}, transactionId={}",
                    request.getPolicyId(), transactionId);

            return PaymentGatewayResponse.builder()
                    .transactionId(transactionId)
                    .status(PaymentResult.SUCCESS)
                    .build();
        }
        //failur reson ligic
        log.warn("Third-party processor returned FAILURE for policyId={}, reason={}",
                request.getPolicyId(), request.getFailureReason());

        return PaymentGatewayResponse.builder()
                .transactionId(null)
                .status(PaymentResult.FAILURE)
                .build();
    }
       //chekcing if the transaction is eligible for retry
    public boolean isRetryEligible(FailureReason failureReason) {
        return failureReason == FailureReason.PROCESSOR_TIMEOUT
                || failureReason == FailureReason.NETWORK_ERROR;
    }
}