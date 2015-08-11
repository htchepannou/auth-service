package com.tchepannou.auth.controller;

import com.tchepannou.auth.exception.AccessTokenException;
import com.tchepannou.auth.exception.AccessTokenExpiredException;
import com.tchepannou.auth.rr.AccessTokenResponse;
import com.tchepannou.auth.service.AccessTokenService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(basePath = "/access_token", value = "AccessToken", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccessTokenController {
    //-- Attribute
    @Autowired
    private AccessTokenService service;

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    @ApiOperation("Returns access token")
    public AccessTokenResponse get (@PathVariable("id") long id) throws AccessTokenException{
        return service.findById(id);
    }

    //-- Error handler
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void notFound (){

    }

    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenExpiredException.class)
    public void expired (){

    }
}
