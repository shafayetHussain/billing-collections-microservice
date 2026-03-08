package com.example.billingcollections.entity;

import com.example.billingcollections.enums.PolicyStatus;

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
@Table(name= "policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {
	@Id
	@Column(name ="policy_id", nullable=false, length=50)
	private String policyId;
	
	@Column(name="customer_id", nullable=false, length=50)
	private String customerId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable=false, length=20)
	private PolicyStatus status;

}
