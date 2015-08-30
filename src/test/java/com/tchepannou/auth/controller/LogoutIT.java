package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.tchepannou.auth.Starter;
import com.tchepannou.auth.auth.AuthServer;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.jms.AuthEventReceiver;
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

import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

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

        final Date now = new Date ();

        // @formatter:off
        given()
                .header(Http.HEADER_TRANSACTION_ID, "2093209")
        .when()
            .post("/v1/auth/logout/123")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_OK)
        ;
        // @formatter:on

        assertThat(AuthEventReceiver.lastEvent.getAccessTokenId()).isEqualTo("123");
        assertThat(AuthEventReceiver.lastEvent.getType()).isEqualTo(AuthConstants.EVENT_LOGOUT);
        assertThat(AuthEventReceiver.lastEvent.getTransactionId()).isEqualTo("2093209");
        assertThat(AuthEventReceiver.lastEvent.getDate()).isAfterOrEqualsTo(now);
    }

    @Test
    public void should_return_409_when_connection_failed () throws Exception{
        // @formatter:off
        given()
                .header(Http.HEADER_TRANSACTION_ID, "2093209")
        .when()
            .post("/v1/auth/logout")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_CONFLICT)
            .body("code", is(409))
            .body("text", is(AuthConstants.ERROR_AUTH_FAILED))
        ;
        // @formatter:on
    }
}
