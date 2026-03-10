package com.example.billingcollections.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.example.billingcollections.enums.FailureReason;
import com.example.billingcollections.enums.PaymentMethod;
import com.example.billingcollections.enums.PaymentResult;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="payment_attempt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAttempt {
	@Id
	@Column(name="attempt_id", nullable=false, length=50)
	private String attemptId;
	
	@Column(name="policy_id", nullable=false, length=50)
	private String policyId;
	
	@Column(name = "installment_no", nullable =false)
	private Integer installmentNo;
	
	@Column(name ="amount", nullable =false, precision=12, scale=2)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(name="payment_method",nullable =false, length=30)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	@Column(name="result", nullable = false, length=20)
	private PaymentResult result;
	
	@Enumerated(EnumType.STRING)
	@Column(name ="failure_reason", length=50)
	private FailureReason failureReason;
	
	@Column(name="idempotency_key", nullable =false, unique=true, length=100)
	private String idempotencyKey;
	
	@Column(name = "external_transaction_id", length = 100)
    private String externalTransactionId;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;

    @Column(name = "retry_eligible", nullable = false)
    private Boolean retryEligible;
		

}
