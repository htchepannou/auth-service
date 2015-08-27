package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.tchepannou.auth.Starter;
import com.tchepannou.auth.auth.AuthServer;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
public class HealthCheckIT {
    @Value ("${server.port}")
    private int port;

    @Value("${insidesoccer.port}")
    private int isPort;

    private AuthServer server;

    @Before
    public void setUp (){
        RestAssured.port = port;
        server = new AuthServer();
    }

    @After
    public void tearDown() throws Exception{
        server.stop();
    }

    @Test
    public void should_success () throws Exception{
        server.start(isPort, new AuthServer.OKHandler("/health/success.html"));
        Thread.sleep(1000);

        // @formatter:off
        when()
            .get("/health")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_OK)
            .body("status", is("UP"))
        ;
        // @formatter:on
    }


    @Test
    public void should_return_503_when_insidesoccer_not_available () throws Exception{
        // @formatter:off
        when()
            .get("/health")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_SERVICE_UNAVAILABLE)
            .body("status", is("DOWN"))
        ;
        // @formatter:on
    }
}
