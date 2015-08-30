package com.tchepannou.auth.jms;

import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.AuthEvent;
import org.springframework.jms.annotation.JmsListener;

public class AuthEventReceiver {
    public static AuthEvent lastEvent = null;

    @JmsListener(destination = AuthConstants.QUEUE_EVENT_LOG, containerFactory = "jmsContainerFactory")
    public void receiveMessage(AuthEvent event) {
        lastEvent = event;
    }
}
