INSERT INTO policy (policy_id, customer_id, status) VALUES
('POL123', 'CUST1001', 'ACTIVE'),
('POL456', 'CUST1002', 'ACTIVE');

INSERT INTO premium_schedule (policy_id, installment_no, due_date, amount_due, status) VALUES
('POL123', 1, DATE '2026-01-10', 150.00, 'PAID'),
('POL123', 2, DATE '2026-02-10', 150.00, 'OVERDUE'),
('POL123', 3, DATE '2026-03-10', 150.00, 'DUE'),
('POL456', 1, DATE '2026-01-05', 200.00, 'OVERDUE'),
('POL456', 2, DATE '2026-02-05', 200.00, 'DUE');

INSERT INTO delinquency_record (policy_id, days_past_due, delinquency_status, last_evaluated_at) VALUES
('POL123', 12, 'DELINQUENT', CURRENT_TIMESTAMP()),
('POL456', 25, 'COLLECTIONS', CURRENT_TIMESTAMP());

INSERT INTO payment_attempt (attempt_id, policy_id, installment_no, amount, payment_method, result, failure_reason, idempotency_key, external_transaction_id, recorded_at, retry_eligible) VALUES
('ATT-1001', 'POL123', 2, 150.00, 'CARD', 'FAILURE', 'PROCESSOR_TIMEOUT', 'idem-1001', NULL, CURRENT_TIMESTAMP(), TRUE);

INSERT INTO retry_task (original_attempt_id, retry_attempt_id, retry_count, status, scheduled_at) VALUES
('ATT-1001', NULL, 0, 'PENDING', CURRENT_TIMESTAMP());