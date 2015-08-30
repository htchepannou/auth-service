package com.tchepannou.auth.service.command;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Strings;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.AuthEvent;
import com.tchepannou.auth.service.Command;
import com.tchepannou.auth.service.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.Resource;
import java.io.IOException;

public abstract class AbstractCommand<I, O> implements Command<I, O> {
    //-- Attributes
    private Logger logger;  // NOSONAR

    @Autowired
    private MetricRegistry metrics;

    @Resource
    private JmsTemplate jmsTemplate;

    //-- Constructor
    public AbstractCommand(){
        this.logger = LoggerFactory.getLogger(getClass());
    }

    protected AbstractCommand(MetricRegistry metrics, JmsTemplate jmsTemplate){
        this();

        this.metrics = metrics;
        this.jmsTemplate = jmsTemplate;
    }

    //-- Abstract
    protected abstract O doExecute (I request, CommandContext context) throws IOException;

    protected abstract String getMetricName ();

    protected abstract String getEventName();


    //-- Command Override
    @Override
    public O execute(I request, CommandContext context) throws IOException {
        final String metricName = getMetricName();
        final Timer.Context timer = metrics.timer(metricName + "-duration").time();
        try {
            metrics.meter(metricName).mark();

            /* execute */
            O response = doExecute(request, context);

            /* post */
            logEvent(request, response, context);
            return response;
        } catch (RuntimeException e) {
            metrics.meter(metricName + "-errors").mark();

            throw e;
        } finally {
            timer.stop();
        }
    }

    //-- Protected
    protected Logger getLogger () {
        return logger;
    }


    protected void logEvent (I request, O response, CommandContext context) {
        String name = getEventName();
        if (Strings.isNullOrEmpty(name)){
            return;
        }


        String id = context.getAccessTokenId();
        if (id != null) {
            AuthEvent event = new AuthEvent(id, name, context.getTransactionId());
            jmsTemplate.send(AuthConstants.QUEUE_EVENT_LOG, session -> session.createObjectMessage(event));
        }
    }
}
