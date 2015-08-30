package com.tchepannou.auth.service.command;

import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.service.CommandContext;
import com.tchepannou.auth.service.LoginService;
import com.tchepannou.auth.service.LogoutCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class LogoutCommandImpl extends AbstractCommand<Void, Void> implements LogoutCommand {
    @Autowired
    private LoginService loginService;

    @Override
    protected Void doExecute(Void request, CommandContext context) throws IOException {
        loginService.logout(context.getAccessTokenId());
        return null;
    }

    @Override
    protected String getMetricName() {
        return AuthConstants.METRIC_LOGOUT;
    }

    @Override
    protected String getEventName() {
        return AuthConstants.EVENT_LOGOUT;
    }
}
