package com.tchepannou.auth.service;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.LoginRequest;

import java.io.IOException;

public interface LoginService {
    AccessTokenResponse login(LoginRequest request) throws IOException;
    void logout (String accessTokenId) throws IOException;
}
