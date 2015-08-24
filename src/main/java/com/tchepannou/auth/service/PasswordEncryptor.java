package com.tchepannou.auth.service;

public interface PasswordEncryptor {
    String encrypt(String password);
    boolean matches(String password, String encrypedPassword);
}
