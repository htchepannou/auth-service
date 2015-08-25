package com.tchepannou.auth.service.is;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.service.LoginService;

public class ISLoginServiceImpl implements LoginService {
    //-- Attributes
    @Override
    public AccessTokenResponse login(LoginRequest request) {
        return new AccessTokenResponse();
    }

    @Override
    public void logout(String accessTokenId) {
        System.out.println("foo");
    }
}
