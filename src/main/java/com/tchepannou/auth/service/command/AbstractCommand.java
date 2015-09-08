package com.tchepannou.auth.service.command;

import com.google.common.base.Strings;
import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.AuthEvent;
import com.tchepannou.auth.service.Command;
import com.tchepannou.auth.service.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.Resource;
import java.io.IOException;

public abstract class AbstractCommand<I, O> implements Command<I, O> {
    //-- Attributes
    private Logger logger;  // NOSONAR

    @Resource
    private JmsTemplate jmsTemplate;

    //-- Constructor
    public AbstractCommand(){
        this.logger = LoggerFactory.getLogger(getClass());
    }

    protected AbstractCommand(JmsTemplate jmsTemplate){
        this();

        this.jmsTemplate = jmsTemplate;
    }

    //-- Abstract
    protected abstract O doExecute (I request, CommandContext context) throws IOException;

    protected abstract String getEventName();


    //-- Command Override
    @Override
    public O execute(I request, CommandContext context) throws IOException {
        /* execute */
        O response = doExecute(request, context);

        /* post */
        logEvent(response, context);
        return response;
    }

    //-- Protected
    protected void logEvent (O response, CommandContext context) {
        String name = getEventName();
        if (Strings.isNullOrEmpty(name)){
            return;
        }


        String id = null;
        if (response instanceof AccessTokenResponse){
            id = ((AccessTokenResponse)response).getId();
        } else {
            id = context.getAccessTokenId();
        }

        if (id != null) {
            AuthEvent event = new AuthEvent(id, name, context.getTransactionId());
            jmsTemplate.send(AuthConstants.QUEUE_EVENT_LOG, session -> session.createObjectMessage(event));
        }
    }
}
