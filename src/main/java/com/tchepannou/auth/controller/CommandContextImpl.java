package com.tchepannou.auth.controller;

import com.tchepannou.auth.service.CommandContext;

public class CommandContextImpl implements CommandContext {
    //-- Attributes
    private String accessTokenId;
    private String transactionId;

    //-- Public
    public CommandContextImpl withAccessTokenId(String id){
        this.accessTokenId = id;
        return this;
    }

    public CommandContextImpl withTransactionId (String transactionId){
        this.transactionId = transactionId;
        return this;
    }

    //-- CommandContext overrides
    @Override public String getAccessTokenId() {
        return accessTokenId;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}
