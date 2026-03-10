package com.example.billingcollections.exception;

/**
 * Thrown when a requested operation is not allowed in the current state.
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}