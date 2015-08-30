package com.tchepannou.auth.service;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.LoginRequest;

public interface LogoutCommand extends Command<LoginRequest, AccessTokenResponse> {
}
