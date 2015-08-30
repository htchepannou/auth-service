package com.tchepannou.auth.service.command;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.service.CommandContext;
import com.tchepannou.auth.service.LoginCommand;
import com.tchepannou.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class LoginCommandImpl extends AbstractCommand<LoginRequest, AccessTokenResponse> implements LoginCommand{
    @Autowired
    private LoginService loginService;

    @Override
    protected AccessTokenResponse doExecute(LoginRequest request, CommandContext context) throws IOException{
        return loginService.login(request);
    }

    @Override
    protected String getMetricName() {
        return AuthConstants.EVENT_LOGIN;
    }

    @Override
    protected String getEventName() {
        return AuthConstants.METRIC_LOGIN;
    }
}
