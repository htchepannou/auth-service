package com.tchepannou.auth.controller;

import com.jayway.restassured.RestAssured;
import com.tchepannou.auth.Starter;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
@Sql({"/db/clean.sql", "/db/login.sql"})
public class AccessTokenControllerIT {
    @Value("${server.port}")
    private int port;

    @Before
    public void setUp (){
        RestAssured.port = port;
    }

    @Test
    public void test_NotFound(){
        // @formatter:off
        when()
            .get("/api/access_token/999")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_Inactive(){
        // @formatter:off
        when()
            .get("/auth/v1/access_token/{id}", "100")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_LoggedOut(){
        // @formatter:off
        when()
            .get("/auth/v1/access_token/101")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED)
            .log()
                .all()
        ;
        // @formatter:on
    }

    @Test
    public void test_get(){
        // @formatter:off
        when()
            .get("/auth/v1/access_token/200")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userId", is(200))
            .body("created", notNullValue())
        ;
        // @formatter:on
    }
}
