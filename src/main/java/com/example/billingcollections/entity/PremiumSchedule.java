package com.example.billingcollections.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.billingcollections.enums.ScheduleStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="premium_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumSchedule {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name= "policy_id", nullable =false, length=50)
	private String policyId;
	
	@Column(name="installment_no", nullable=false)
	private Integer installmentNo;
	
	@Column (name ="due_date", nullable =false)
	private LocalDate dueDate;
	
	@Column(name = "amount_due", nullable =false, precision =12, scale=2)
	private BigDecimal amountDue;
	
	@Enumerated(EnumType.STRING)
	@Column(name ="status", nullable=false, length=20)
	private ScheduleStatus status;

}
