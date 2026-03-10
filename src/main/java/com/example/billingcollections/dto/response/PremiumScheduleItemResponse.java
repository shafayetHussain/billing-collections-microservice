package com.example.billingcollections.dto.response;

import com.example.billingcollections.enums.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PremiumScheduleItemResponse {

    private Integer installmentNo;
    private LocalDate dueDate;
    private BigDecimal amountDue;
    private ScheduleStatus status;
}