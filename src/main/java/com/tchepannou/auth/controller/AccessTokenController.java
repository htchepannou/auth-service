package com.tchepannou.auth.controller;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.exception.AccessTokenException;
import com.tchepannou.auth.service.AccessTokenService;
import com.tchepannou.core.client.v1.ErrorResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(basePath = "/v1/access_token", value = "AccessToken", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccessTokenController extends AbstractController{
    //-- Attribute
    @Autowired
    private AccessTokenService service;

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    @ApiOperation(value="Get AccessToken", notes="Returns an access token by it's ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 404, message = "Access token not found"),
            @ApiResponse(code = 401, message = "Access token has expired")
    })
    public AccessTokenResponse get (@PathVariable("id") String id) throws AccessTokenException{
        return service.findById(id);
    }

    //-- Error handler
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenException.class)
    public ErrorResponse expired (AccessTokenException exception, HttpServletRequest request){
        return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request);
    }
}
