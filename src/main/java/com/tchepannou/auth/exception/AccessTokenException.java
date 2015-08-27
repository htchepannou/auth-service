package com.tchepannou.auth.exception;

public class AccessTokenException extends RuntimeException {
    public AccessTokenException(String message) {
        super(message);
    }
    public AccessTokenException(String message, Throwable e) {
        super(message);
    }
}
