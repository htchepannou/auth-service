package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
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
import com.tchepannou.auth.Starter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
public class HealthCheckIT extends AbstractHandler {
    @Value ("${server.port}")
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

    @Test
    public void should_success () throws Exception{
        Server server = new Server(isPort);
        server.setHandler(this);
        server.start();
        Thread.sleep(2000);
        try {
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
        } finally {
            server.stop();
        }
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
