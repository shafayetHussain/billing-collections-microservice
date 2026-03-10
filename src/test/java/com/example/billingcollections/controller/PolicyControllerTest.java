package com.example.billingcollections.controller;

import com.example.billingcollections.config.SecurityConfig;
import com.example.billingcollections.dto.response.DelinquentPoliciesResponse;
import com.example.billingcollections.dto.response.PremiumScheduleResponse;
import com.example.billingcollections.service.DelinquencyService;
import com.example.billingcollections.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PolicyController.class)
@Import(SecurityConfig.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    @MockBean
    private DelinquencyService delinquencyService;

    @Test
    @WithMockUser
    void shouldReturnPremiumSchedule() throws Exception {
        when(policyService.getPremiumSchedule("POL123"))
                .thenReturn(PremiumScheduleResponse.builder()
                        .policyId("POL123")
                        .currency("USD")
                        .build());

        mockMvc.perform(get("/api/v1/policies/POL123/premium-schedule"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnDelinquentPoliciesForAuthenticatedUser() throws Exception {
        when(delinquencyService.getDelinquentPolicies(10))
                .thenReturn(DelinquentPoliciesResponse.builder()
                        .count(0)
                        .build());

        mockMvc.perform(get("/api/v1/policies/delinquent").param("daysPastDue", "10"))
                .andExpect(status().isOk());
    }
}