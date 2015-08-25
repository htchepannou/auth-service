package com.tchepannou.auth.exception;

public class AccessTokenException extends RuntimeException {
    public AccessTokenException(String message) {
        super(message);
    }
}
