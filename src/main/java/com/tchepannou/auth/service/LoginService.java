package com.tchepannou.auth.service;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.LoginRequest;

public interface LoginService {
    AccessTokenResponse login(LoginRequest request);
    void logout (String accessTokenId);
}
