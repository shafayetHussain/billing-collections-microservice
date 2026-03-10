package com.example.billingcollections.dto.response;

import com.example.billingcollections.enums.PaymentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGatewayResponse {

    private String transactionId;
    private PaymentResult status;
}