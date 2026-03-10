package com.example.billingcollections.controller;

import com.example.billingcollections.config.SecurityConfig;
import com.example.billingcollections.dto.request.PaymentAttemptRequest;
import com.example.billingcollections.dto.response.PaymentAttemptResponse;
import com.example.billingcollections.dto.response.RetryResponse;
import com.example.billingcollections.enums.PaymentMethod;
import com.example.billingcollections.enums.PaymentResult;
import com.example.billingcollections.enums.RetryStatus;
import com.example.billingcollections.service.PaymentService;
import com.example.billingcollections.service.RetryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import(SecurityConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private RetryService retryService;

    @Test
    @WithMockUser
    void shouldRecordPaymentAttempt() throws Exception {
        PaymentAttemptRequest request = PaymentAttemptRequest.builder()
                .policyId("POL123")
                .installmentNo(2)
                .amount(new BigDecimal("150.00"))
                .paymentMethod(PaymentMethod.CARD)
                .result(PaymentResult.SUCCESS)
                .idempotencyKey("idem-123")
                .build();

        when(paymentService.recordPaymentAttempt(request))
                .thenReturn(PaymentAttemptResponse.builder()
                        .attemptId("ATT-1001")
                        .policyId("POL123")
                        .result(PaymentResult.SUCCESS)
                        .recordedAt(OffsetDateTime.now())
                        .build());

        mockMvc.perform(post("/api/v1/payments/attempts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void shouldTriggerRetryForAuthenticatedUser() throws Exception {
        when(retryService.triggerRetry("ATT-1001"))
                .thenReturn(RetryResponse.builder()
                        .originalAttemptId("ATT-1001")
                        .retryAttemptId("ATT-2001")
                        .status(RetryStatus.RETRY_TRIGGERED)
                        .scheduledAt(OffsetDateTime.now())
                        .build());

        mockMvc.perform(post("/api/v1/payments/attempts/ATT-1001/retry"))
                .andExpect(status().isOk());
    }
}