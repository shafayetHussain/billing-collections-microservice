# Billing Collections Service

## Overview

Billing Collections Service is a Spring Boot microservice that simulates a billing and collections engine for insurance policies.

The service provides REST APIs to:

* Retrieve premium schedules for policies
* Record payment attempts and results
* Identify delinquent policies
* Trigger retry actions for failed payments

The system also simulates integration with a third-party payment processor.

---

# Technology Stack

* Java 17
* Spring Boot 3
* Spring Security (HTTP Basic Authentication)
* Spring Data JPA
* H2 In-Memory Database
* OpenAPI / Swagger
* Maven
* JUnit 5 & Mockito
* Spring Boot Actuator

---

# System Design

The system follows a layered microservice architecture.

Client → Controller → Service → Repository → Database

An internal **PaymentGatewayClient** simulates sending payment processing to a third-party processor.

### Key Components

**Controller Layer**

Handles incoming REST API requests.

**Service Layer**

Contains business logic including:

* Payment processing
* Retry eligibility
* Delinquency detection

**Repository Layer**

Uses Spring Data JPA to access the database.

**External Payment Gateway**

Simulated component representing a third-party payment processor.

---

# Architecture Diagram

The architecture diagram is included in this repository:

```
system-design-diagram.pdf
```

---

# Security

The application uses **HTTP Basic Authentication**.

Demo credentials:

```
username: user
password: user123
```

All `/api/v1/**` endpoints require authentication.

Public endpoints:

* Swagger UI
* Health Check
* H2 Console

---

# Requirements

Before running the application ensure you have:

* Java 17
* Maven 3+

---

# Running the Application

### 1. Clone the repository

```
https://github.com/shafayetHussain/billing-collections-microservice.git
```

or download the ZIP file.

---

### 2. Navigate to the project directory

```
cd billing-collections-service
```

---

### 3. Build the project

```
mvn clean install
```

This compiles the application and runs all tests.

---

### 4. Start the application

```
mvn spring-boot:run
```

Application starts at:

```
http://localhost:8080
```

---

# Verify the Application

## Swagger API Documentation

```
http://localhost:8080/swagger-ui/index.html
```

Swagger allows interactive testing of all endpoints.

---

# Health Check

```
GET http://localhost:8080/actuator/health
```

Expected response:

```
{
 "status": "UP"
}
```

---

# H2 Database Console

```
http://localhost:8080/h2-console
```

Connection configuration:

| Property | Value                 |
| -------- | --------------------- |
| JDBC URL | jdbc:h2:mem:billingdb |
| Username | sa                    |
| Password | (leave blank)         |

---

# API Endpoints

## 1. Retrieve Premium Schedule

```
GET /api/v1/policies/{policyId}/premium-schedule
```

Example:

```
GET /api/v1/policies/POL123/premium-schedule
```

---

# 2. Record Payment Attempt

```
POST /api/v1/payments/attempts
```

Example request:

```
{
 "policyId": "POL123",
 "installmentNo": 1,
 "amount": 150.00,
 "paymentMethod": "CARD",
 "result": "SUCCESS",
 "idempotencyKey": "payment-test-1"
}
```

Example success response:

```
{
 "attemptId": "ATT-xxxxx",
 "policyId": "POL123",
 "result": "SUCCESS",
 "retryEligible": false,
 "transactionId": "txn-xxxxx"
}
```

---

# Payment Failure and Retry Eligibility

If the payment result is **FAILURE**, the system determines whether the failure is retryable.

Retry eligibility depends on the failure reason.

Example failure request:

```
{
 "policyId": "POL123",
 "installmentNo": 2,
 "amount": 150.00,
 "paymentMethod": "CARD",
 "result": "FAILURE",
 "failureReason": "PROCESSOR_TIMEOUT",
 "idempotencyKey": "payment-test-2"
}
```

Example response:

```
{
 "attemptId": "ATT-1001",
 "policyId": "POL123",
 "result": "FAILURE",
 "retryEligible": true
}
```

If `retryEligible = true`, the payment attempt can be retried.

---

# 3. Retry Failed Payment

```
POST /api/v1/payments/attempts/{attemptId}/retry
```

Important notes:

* `attemptId` **must already exist** in the `PAYMENT_ATTEMPT` table.
* The attempt must be **retryEligible = true**.

Example:

```
POST /api/v1/payments/attempts/ATT-1001/retry
```

Example response:

```
{
 "originalAttemptId": "ATT-1001",
 "retryAttemptId": "ATT-2001",
 "status": "RETRY_TRIGGERED"
}
```

If the attempt does not exist, the API returns:

```
404 Payment attempt not found
```

---

# 4. Retrieve Delinquent Policies

```
GET /api/v1/policies/delinquent?daysPastDue=10
```

Example response:

```
{
 "count": 1,
 "policies": [...]
}
```

---

# Logging

Application logs are written to:

```
logs/billing-collections-service.log
```

Each request includes a **requestId** for traceable logging.

---

# Idempotency

Payment attempts support idempotency using:

```
idempotencyKey
```

If the same key is submitted again, the service returns the **previously recorded payment attempt** instead of creating a duplicate.

---

# Running Tests

Execute all tests with:

```
mvn test
```

Test coverage includes:

* Service layer tests
* Controller tests
* Retry logic
* Idempotency behavior

---

# Project Structure

```
controller
service
repository
entity
dto
config
exception
filter
client
```

---

# Notes

* Payment gateway is simulated to represent a third-party processor.
* H2 in-memory database is used for simplicity of evaluation.
* Swagger UI is included for quick API testing.
