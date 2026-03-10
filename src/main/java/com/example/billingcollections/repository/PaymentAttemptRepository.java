package com.example.billingcollections.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billingcollections.entity.PaymentAttempt;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt,String>{
	Optional<PaymentAttempt>findByIdempotencyKey(String idempotencyKey);

}
