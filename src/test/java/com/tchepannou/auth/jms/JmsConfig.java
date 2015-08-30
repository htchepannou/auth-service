package com.tchepannou.auth.jms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    @Bean AuthEventReceiver eventLogReceiver(){
        return new AuthEventReceiver();
    }
}
