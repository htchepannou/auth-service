package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.tchepannou.auth.Starter;
import com.tchepannou.auth.auth.AuthServer;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.jms.AuthEventReceiver;
import com.tchepannou.core.http.Http;
import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
public class LoginIT {
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

        AuthEventReceiver.lastEvent = null;
    }


    //-- Tests
    @Test
    public void should_login () throws Exception{
        final AbstractHandler handler = new AbstractHandler() {
            @Override public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
                    throws IOException, ServletException {
                if (s.startsWith("/is-api-web/login/signin.json")){
                    new AuthServer.OKHandler("/login/success.json").handle(s, request, httpServletRequest, httpServletResponse);
                } else if (s.startsWith("/is-api-web/login/show.json")){
                    new AuthServer.OKHandler("/login/access_token.json").handle(s, request, httpServletRequest, httpServletResponse);
                }
                request.setHandled(true);
            }
        };
        server.start(isPort, handler);

        final LoginRequest request = new LoginRequest();
        request.setUsername("foo");
        request.setPassword("fdlkfdl");

        final Date now = new Date ();

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
                .header(Http.HEADER_TRANSACTION_ID, "fdlkfldk")
        .when()
            .post("/v1/auth/login")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_OK)
            .body("id", is("466500"))
            .body("userId", is(20176))
            .body("created", startsWith("2015-08-27"))
        ;
        // @formatter:on


        assertThat(AuthEventReceiver.lastEvent).isNotNull();
        assertThat(AuthEventReceiver.lastEvent.getAccessTokenId()).isEqualTo("466500");
        assertThat(AuthEventReceiver.lastEvent.getType()).isEqualTo(AuthConstants.EVENT_LOGIN);
        assertThat(AuthEventReceiver.lastEvent.getTransactionId()).isEqualTo("fdlkfldk");
        assertThat(AuthEventReceiver.lastEvent.getDate()).isAfterOrEqualsTo(now);
    }

    @Test
    public void should_return_400_when_no_username () throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("fdlkfdl");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
                .header(Http.HEADER_TRANSACTION_ID, "fdlkfldk")
        .when()
            .post("/v1/auth/login")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("code", is(400))
            .body("text", is("username"))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_400_when_no_password () throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("fdkl");
        request.setPassword("");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
                .header(Http.HEADER_TRANSACTION_ID, "fdlkfldk")
        .when()
            .post("/v1/auth/login")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("code", is(400))
            .body("text", is("password"))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_409_when_server_not_available () throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("foo");
        request.setPassword("fdlkfdl");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
                .header(Http.HEADER_TRANSACTION_ID, "fdlkfldk")
        .when()
            .post("/v1/auth/login")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_CONFLICT)
            .body("code", is(409))
            .body("text", is(AuthConstants.ERROR_IO))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_409_when_auth_failed () throws Exception {
        server.start(isPort, new AuthServer.OKHandler("/login/auth_failed.json"));

        LoginRequest request = new LoginRequest();
        request.setUsername("foo");
        request.setPassword("fdlkfdl");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
                .header(Http.HEADER_TRANSACTION_ID, "fdlkfldk")
        .when()
            .post("/v1/auth/login")
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
