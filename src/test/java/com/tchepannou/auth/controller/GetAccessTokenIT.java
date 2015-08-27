package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.tchepannou.auth.Starter;
import com.tchepannou.auth.auth.AuthServer;
import com.tchepannou.auth.client.v1.AuthErrors;
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
public class GetAccessTokenIT {
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

    @Test
    public void should_return_access_token() throws Exception{
        server.start(isPort, new AuthServer.OKHandler("/access_token/success.json"));

        // @formatter:off
        when()
            .get("/v1/access_token/200")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("id", is("466347"))
            .body("userId", is(20176))
            .body("created", startsWith("2015-08-27"))
            .body("logoutDate", nullValue())
        ;
        // @formatter:on
    }

    @Test
    public void should_return_401_when_expired() throws Exception{
        server.start(isPort, new AuthServer.OKHandler("/access_token/expired.json"));

        // @formatter:off
        when()
            .get("/v1/access_token/999")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .log()
                .all()
            .body("code", is(HttpStatus.SC_UNAUTHORIZED))
            .body("text", is(AuthErrors.TOKEN_EXPIRED))
        ;
        // @formatter:on
    }


    @Test
    public void should_return_401_when_logged_out() throws Exception{
        server.start(isPort, new AuthServer.OKHandler("/access_token/logout.json"));

        // @formatter:off
        when()
            .get("/v1/access_token/101")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .log()
                .all()
            .body("code", is(HttpStatus.SC_UNAUTHORIZED))
            .body("text", is(AuthErrors.TOKEN_EXPIRED))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_401_when_not_found() throws Exception{
        server.start(isPort, new AuthServer.FailHandler(404));

        // @formatter:off
        when()
            .get("/v1/access_token/{id}", "100")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .log()
                .all()
            .body("code", is(HttpStatus.SC_UNAUTHORIZED))
            .body("text", is(AuthErrors.TOKEN_NOT_FOUND))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_500_when_io_error() throws Exception{
        // @formatter:off
        when()
            .get("/v1/access_token/{id}", "100")
        .then()
            .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .log()
                .all()
            .body("code", is(HttpStatus.SC_INTERNAL_SERVER_ERROR))
            .body("text", is(AuthErrors.IO_ERROR))
        ;
        // @formatter:on
    }
}
