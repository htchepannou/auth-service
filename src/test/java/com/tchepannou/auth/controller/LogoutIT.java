package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.tchepannou.auth.Starter;
import com.tchepannou.core.http.Http;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
public class LogoutIT extends AbstractHandler {
    @Value("${server.port}")
    private int port;

    @Value("${insidesoccer.port}")
    private int isPort;

    @Before
    public void setUp (){
        RestAssured.port = port;
    }


    //-- AbstractHandler overrides
    @Override
    public void handle(String uri, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        request.setHandled(true);
    }

    //-- Tests
    @Test
    public void should_logout () throws Exception{
        Server server = new Server(isPort);
        server.setHandler(this);
        server.start();

        try{
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

        } finally {
            server.stop();
        }
    }
}
