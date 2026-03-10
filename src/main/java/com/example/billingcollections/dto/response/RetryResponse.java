package com.example.billingcollections.dto.response;

import com.example.billingcollections.enums.RetryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryResponse {

    private String originalAttemptId;
    private String retryAttemptId;
    private RetryStatus status;
    private OffsetDateTime scheduledAt;
}