package com.tchepannou.auth.config;

import com.tchepannou.auth.service.AccessTokenService;
import com.tchepannou.auth.service.LoginService;
import com.tchepannou.auth.service.PermissionService;
import com.tchepannou.auth.service.is.ISAccessTokenService;
import com.tchepannou.auth.service.is.ISHealthIndicator;
import com.tchepannou.auth.service.is.ISLoginServiceImpl;
import com.tchepannou.auth.service.is.ISPermissionService;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IsConfig {
    @Bean
    AccessTokenService accessTokenService(){
        return new ISAccessTokenService();
    }

    @Bean
    PermissionService permissionService () {
        return new ISPermissionService();
    }

    @Bean
    LoginService loginService(){
        return new ISLoginServiceImpl();
    }

    @Bean
    HealthIndicator healthIndicator () {
        return new ISHealthIndicator();
    }
}
