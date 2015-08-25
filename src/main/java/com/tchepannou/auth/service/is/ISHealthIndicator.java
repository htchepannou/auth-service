package com.tchepannou.auth.service.is;

import com.tchepannou.core.http.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.io.IOException;

public class ISHealthIndicator implements HealthIndicator {
    private static final Logger LOG = LoggerFactory.getLogger(ISHealthIndicator.class);

    @Value("${insidesoccer.hostname}")
    private String hostname;

    @Value("${insidesoccer.port}")
    private int port;


    //-- HealthIndicator overrides
    @Override
    public Health health() {
        try {
            new Http()
                    .withHost(hostname)
                    .withPort(port)
                    .withPath("/is-api-web/help")
                    .get();

            return Health.up().build();
        } catch (IOException e){
            LOG.error("Health check of insidesoccer.com API failed", e);
            return Health
                    .down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
