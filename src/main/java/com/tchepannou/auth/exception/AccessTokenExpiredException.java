package com.tchepannou.auth.exception;

public class AccessTokenExpiredException extends AccessTokenException {
    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
