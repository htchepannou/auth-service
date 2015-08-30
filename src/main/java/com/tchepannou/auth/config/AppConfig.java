package com.tchepannou.auth.config;

import com.tchepannou.auth.service.LoginCommand;
import com.tchepannou.auth.service.LogoutCommand;
import com.tchepannou.auth.service.command.LoginCommandImpl;
import com.tchepannou.auth.service.command.LogoutCommandImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class AppConfig {
    //-- JMS Config
    @Bean JmsListenerContainerFactory jmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean JmsTemplate eventLogQueue(ConnectionFactory factory){
        return new JmsTemplate(factory);
    }

    //-- Commands
    @Bean
    LoginCommand loginCommand () {
        return new LoginCommandImpl();
    }

    @Bean LogoutCommand logoutCommand () {
        return new LogoutCommandImpl();
    }
}
