package com.example.billingcollections.entity;

import com.example.billingcollections.enums.RetryStatus;
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

import java.time.OffsetDateTime;

@Entity
@Table(name = "retry_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_attempt_id", nullable = false, length = 50)
    private String originalAttemptId;

    @Column(name = "retry_attempt_id", length = 50)
    private String retryAttemptId;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RetryStatus status;

    @Column(name = "scheduled_at", nullable = false)
    private OffsetDateTime scheduledAt;
}