package com.example.billingcollections.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billingcollections.entity.PremiumSchedule;

public interface PremiumScheduleRepository extends JpaRepository<PremiumSchedule, Long>{
	List<PremiumSchedule>findByPolicyIdOrderByInstallmentNoAsc(String policyId);

}
