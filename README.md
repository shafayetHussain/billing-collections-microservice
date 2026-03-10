# Billing Collections Service

## Overview

Billing Collections Service is a Spring Boot microservice that simulates a billing and collections engine for insurance policies.

The service provides REST APIs to:

* Retrieve the premium schedule for a policy
* Record payment attempts and results
* Identify delinquent policies
* Trigger retry actions for failed payments

The system also simulates integration with an external payment processor.

---

# Technology Stack

* Java 17
* Spring Boot 3
* Spring Security (HTTP Basic Authentication)
* Spring Data JPA
* H2 In-Memory Database
* Swagger / OpenAPI
* Maven
* JUnit 5 & Mockito
* Spring Boot Actuator (Health Check)

---

# System Design

The system follows a layered microservice architecture.

Client → Controller → Service → Repository → Database

A simulated external payment gateway represents third-party payment processing.

### Key Components

**Controller Layer**

* Exposes REST APIs
* Handles HTTP requests and validation

**Service Layer**

* Implements business logic
* Handles payment retries and delinquency detection

**Repository Layer**

* Uses Spring Data JPA for database interaction

**External Payment Gateway**

* Simulated component representing third-party payment processing

---

# System Architecture Diagram

The architecture diagram is included in this repository:

```
system-design-diagram.pdf
```

# Security

The service uses **HTTP Basic Authentication**.

Demo credentials:

```
username: user
password: user123
```

All `/api/v1/**` endpoints require authentication.

Public endpoints:

* Swagger UI
* Health Check
* H2 Database Console

---

# Requirements

Before running the application ensure you have:

* Java 17
* Maven 3+

---

# Running the Application

### 1. Clone the repository

```
git clone https://github.com/<your-username>/billing-collections-service.git
```

Or download the ZIP file and extract it.

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

This will compile the project and execute all tests.

---

### 4. Run the application

```
mvn spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

# Verify the Application

## Swagger API Documentation

```
http://localhost:8080/swagger-ui/index.html
```

Swagger allows interactive testing of all APIs.

---

## Health Check

```
GET http://localhost:8080/actuator/health
```

Example response:

```
{
  "status": "UP"
}
```

---

## H2 Database Console

```
http://localhost:8080/h2-console
```

Use the following configuration:

| Property | Value                 |
| -------- | --------------------- |
| JDBC URL | jdbc:h2:mem:billingdb |
| Username | sa                    |
| Password | (leave blank)         |

---

# API Endpoints

## Retrieve Premium Schedule

```
GET /api/v1/policies/{policyId}/premium-schedule
```

Example:

```
GET /api/v1/policies/POL123/premium-schedule
```

---

## Record Payment Attempt

```
POST /api/v1/payments/attempts
```

Example request body:

```
{
 "policyId": "POL123",
 "installmentNo": 1,
 "amount": 150.00,
 "paymentMethod": "CARD",
 "result": "SUCCESS",
 "idempotencyKey": "payment-123"
}
```

---

## Retry Failed Payment

```
POST /api/v1/payments/attempts/{attemptId}/retry
```

Example:

```
POST /api/v1/payments/attempts/ATT-123/retry
```

---

## Retrieve Delinquent Policies

```
GET /api/v1/policies/delinquent?daysPastDue=10
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

If the same key is submitted again, the service returns the previously recorded payment attempt instead of creating a duplicate.

---

# Running Tests

Run all tests using:

```
mvn test
```

Tests cover:

* Service layer logic
* Controller endpoints
* Retry behavior
* Idempotency handling

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

* The payment gateway is simulated to represent integration with a third-party processor.
* The H2 database is used for simplicity and easy evaluation.
* Swagger UI is included for quick API testing.
