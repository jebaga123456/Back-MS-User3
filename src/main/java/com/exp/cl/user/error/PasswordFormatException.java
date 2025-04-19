package com.exp.cl.user.error;

public class PasswordFormatException extends RuntimeException {
    public PasswordFormatException(String message) {
        super(message);
    }
}