package com.example.billingcollections.dto.request;

import java.math.BigDecimal;

import com.example.billingcollections.enums.FailureReason;
import com.example.billingcollections.enums.PaymentMethod;
import com.example.billingcollections.enums.PaymentResult;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAttemptRequest {
	@NotBlank(message="Policyid is required")
	private String policyId;
	
	@NotNull(message="installmentNo is required")
	@Min(value=1, message ="InstallmentNo must be at least 1")
	private Integer installmentNo;
	
	@NotNull(message="Amount is required")
	@DecimalMin(value="0.01", message="Amount must be greate than 0")
	private BigDecimal amount;
	
	@NotNull(message="Payment Method is required")
	private PaymentMethod paymentMethod;
	
	@NotNull(message="result is required")
	private PaymentResult result;
	
	
	private FailureReason failureReason;
	
	@NotBlank(message="idempotencyKey is required")
	private String idempotencyKey;
	
	

}
