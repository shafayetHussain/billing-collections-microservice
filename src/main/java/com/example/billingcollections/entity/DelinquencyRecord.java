package com.example.billingcollections.entity;
import java.time.OffsetDateTime;

import com.example.billingcollections.enums.DelinquencyStatus;
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
@Table(name="delinquency_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelinquencyRecord {
	
	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;
	
	    @Column(name = "policy_id", nullable = false, length = 50)
	    private String policyId;

	    @Column(name = "days_past_due", nullable = false)
	    private Integer daysPastDue;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "delinquency_status", nullable = false, length = 30)
	    private DelinquencyStatus delinquencyStatus;

	    @Column(name = "last_evaluated_at", nullable = false)
	    private OffsetDateTime lastEvaluatedAt;
	
	
	

}
