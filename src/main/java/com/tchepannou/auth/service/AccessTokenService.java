package com.tchepannou.auth.service;

import com.tchepannou.auth.client.v1.AccessTokenResponse;

import java.io.IOException;

public interface AccessTokenService {
    AccessTokenResponse findById (String id) throws IOException;
}
