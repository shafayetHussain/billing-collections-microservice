package com.example.billingcollections.repository;

import com.example.billingcollections.entity.RetryTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetryTaskRepository extends JpaRepository<RetryTask, Long> {
}