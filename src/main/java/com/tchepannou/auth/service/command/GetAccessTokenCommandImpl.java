package com.tchepannou.auth.service.command;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.service.AccessTokenService;
import com.tchepannou.auth.service.CommandContext;
import com.tchepannou.auth.service.GetAccessTokenCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class GetAccessTokenCommandImpl extends AbstractCommand<Void, AccessTokenResponse> implements GetAccessTokenCommand {
    @Autowired
    private AccessTokenService service;

    @Override
    protected AccessTokenResponse doExecute(Void request, CommandContext context) throws IOException {
        return service.findById(context.getAccessTokenId());
    }

    @Override
    protected String getEventName() {
        return null;
    }
}
