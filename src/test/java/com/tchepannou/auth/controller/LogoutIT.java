package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.tchepannou.auth.Starter;
import com.tchepannou.auth.auth.AuthServer;
import com.tchepannou.core.http.Http;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
public class LogoutIT {
    @Value("${server.port}")
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


    //-- Tests
    @Test
    public void should_logout () throws Exception{
        server.start(isPort, new AuthServer.OKHandler("/logout/success.json"));

        // @formatter:off
        given()
                .header(Http.HEADER_ACCESS_TOKEN, "212")
        .when()
            .post("/v1/auth/logout")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_OK)
        ;
        // @formatter:on
    }

    @Test
    public void should_return_500_when_connection_failed () throws Exception{
        // @formatter:off
        given()
                .header(Http.HEADER_ACCESS_TOKEN, "212")
        .when()
            .post("/v1/auth/logout")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        ;
        // @formatter:on
    }
}
