package com.example.billingcollections.service;

import com.example.billingcollections.entity.DelinquencyRecord;
import com.example.billingcollections.enums.DelinquencyStatus;
import com.example.billingcollections.repository.DelinquencyRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

// Focused test for delinquency lookups — just one scenario here since the logic
// is straightforward: filter by days past due and wrap the results
@ExtendWith(MockitoExtension.class)
class DelinquencyServiceTest {

    @Mock
    private DelinquencyRecordRepository delinquencyRecordRepository;

    @InjectMocks
    private DelinquencyService delinquencyService;

    @Test
    void shouldReturnDelinquentPolicies() {
        // 12 days past due — sits above the 10-day threshold we're querying with,
        // so this record should definitely show up in the results
        DelinquencyRecord record = DelinquencyRecord.builder()
                .id(1L)
                .policyId("POL123")
                .daysPastDue(12)
                .delinquencyStatus(DelinquencyStatus.DELINQUENT)
                .lastEvaluatedAt(OffsetDateTime.now())  // evaluated recently, status is current
                .build();

        // The repository does the heavy lifting with a >= filter —
        // we just need to confirm the service passes the threshold through correctly
        when(delinquencyRecordRepository.findByDaysPastDueGreaterThanEqual(10))
                .thenReturn(List.of(record));

        var response = delinquencyService.getDelinquentPolicies(10);

        // Count should reflect the list size, and policy details should
        // map through without getting dropped or mangled
        assertEquals(1, response.getCount());
        assertEquals("POL123", response.getPolicies().get(0).getPolicyId());
    }
}