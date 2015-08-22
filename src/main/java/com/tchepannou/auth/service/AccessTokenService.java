package com.tchepannou.auth.service;

import com.tchepannou.auth.exception.AccessTokenException;
import com.tchepannou.auth.client.v1.AccessTokenResponse;

public interface AccessTokenService {
    AccessTokenResponse findById (String id) throws AccessTokenException;
}
