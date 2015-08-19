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
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebIntegrationTest
@Sql({"/db/clean.sql", "/db/permission.sql"})
public class GetPermissionCollectionIT {
    @Value("${server.port}")
    private int port;

    @Before
    public void setUp (){
        RestAssured.port = port;
    }


    @Test
    public void should_return_permissions(){
        // @formatter:off
        when()
            .get("/v1/permission/user/100/space/1000/app/app1")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userId", is(100))
            .body("spaceId", is(1000))
            .body("application", is("app1"))
            .body("permissions", hasSize(3))
            .body("permissions", hasItems("app1-add", "app1-edit", "app1-delete"))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_no_permission_when_bad_user(){
        // @formatter:off
        when()
            .get("/v1/permission/user/9999/space/1000/app/app1")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userId", is(9999))
            .body("spaceId", is(1000))
            .body("application", is("app1"))
            .body("permissions", hasSize(0))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_no_permission_when_bad_space(){
        // @formatter:off
        when()
            .get("/v1/permission/user/100/space/9999/app/app1")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userId", is(100))
            .body("spaceId", is(9999))
            .body("application", is("app1"))
            .body("permissions", hasSize(0))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_no_permission_when_bad_app(){
        // @formatter:off
        when()
            .get("/v1/permission/user/100/space/1000/app/_unknown_")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .log()
                .all()
            .body("userId", is(100))
            .body("spaceId", is(1000))
            .body("application", is("_unknown_"))
            .body("permissions", hasSize(0))
        ;
        // @formatter:on
    }
}
