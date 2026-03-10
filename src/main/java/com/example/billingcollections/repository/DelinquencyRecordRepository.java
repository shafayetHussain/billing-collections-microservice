package com.example.billingcollections.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billingcollections.entity.DelinquencyRecord;

public interface DelinquencyRecordRepository extends JpaRepository<DelinquencyRecord, Long>{
	List<DelinquencyRecord>findByDaysPastDueGreaterThanEqual(Integer daysPastDue);

}
