package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.tchepannou.auth.Starter;
import com.tchepannou.auth.client.v1.LoginRequest;
import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
@Sql({"/db/clean.sql", "/db/login.sql"})
public class LoginIT extends AbstractHandler {
    @Value("${server.port}")
    private int port;

    @Value("${insidesoccer.port}")
    private int isPort;

    private String errorCode = "0";

    @Before
    public void setUp (){
        RestAssured.port = port;
    }


    //-- AbstractHandler overrides
    @Override
    public void handle(String uri, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        request.setHandled(true);
        httpServletResponse.addHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(200);
        httpServletResponse.getWriter().write(
                "{"
                        + "\"error_code\":\"" + errorCode + "\","
                        + "\"login_id\":\"100\""
                + "}"
        );
    }

    //-- Tests
    @Test
    public void should_login () throws Exception{
        Server server = new Server(isPort);
        try{
            server.setHandler(this);
            server.start();

            LoginRequest request = new LoginRequest();
            request.setUsername("foo");
            request.setPassword("fdlkfdl");

            // @formatter:off
            given()
                    .contentType(ContentType.JSON)
                    .content(request, ObjectMapperType.JACKSON_2)
            .when()
                .post("/v1/auth/login")
            .then()
                .log()
                    .all()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is("100"))
                .body("userId", is(200))
                .body("created", notNullValue())
            ;
            // @formatter:on

        } finally {
            server.stop();
        }
    }

    @Test
    public void should_fail_when_auth_failed () throws Exception {
        Server server = new Server(isPort);
        errorCode = "auth_failed";
        try{
            server.setHandler(this);
            server.start();

            LoginRequest request = new LoginRequest();
            request.setUsername("foo");
            request.setPassword("fdlkfdl");

            // @formatter:off
            given()
                    .contentType(ContentType.JSON)
                    .content(request, ObjectMapperType.JACKSON_2)
            .when()
                .post("/v1/auth/login")
            .then()
                .log()
                    .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("code", is(409))
                .body("text", is(errorCode))
            ;
            // @formatter:on

        } finally {
            server.stop();
        }
    }


    @Test
    public void should_fail_when_server_not_available () throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("foo");
        request.setPassword("fdlkfdl");

        // @formatter:off
        given()
                .contentType(ContentType.JSON)
                .content(request, ObjectMapperType.JACKSON_2)
        .when()
            .post("/v1/auth/login")
        .then()
            .log()
                .all()
            .statusCode(HttpStatus.SC_CONFLICT)
            .body("code", is(409))
            .body("text", is("connection_error"))
        ;
        // @formatter:on
    }
}
