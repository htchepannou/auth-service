package com.tchepannou.auth.controller;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.exception.AccessTokenException;
import com.tchepannou.auth.service.GetAccessTokenCommand;
import com.tchepannou.core.client.v1.ErrorResponse;
import com.tchepannou.core.http.Http;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Api(basePath = "/v1/access_token", value = "AccessToken", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccessTokenController extends AbstractController{
    //-- Attribute
    @Autowired
    private GetAccessTokenCommand getAccessTokenCommand;

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    @ApiOperation(value="Get AccessToken", notes="Returns an access token by it's ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 401, message = AuthConstants.ERROR_TOKEN_NOT_FOUND),
            @ApiResponse(code = 401, message = AuthConstants.ERROR_TOKEN_EXPIRED),
            @ApiResponse(code = 401, message = AuthConstants.ERROR_BAD_TOKEN),
    })
    public AccessTokenResponse get (
            @RequestHeader(Http.HEADER_TRANSACTION_ID) String transactionId,
            @PathVariable("id") String id
    ) throws IOException {
        return getAccessTokenCommand.execute(
                null,
                new CommandContextImpl()
                        .withTransactionId(transactionId)
                        .withAccessTokenId(id)
        );
    }

    //-- Error handler
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenException.class)
    public ErrorResponse tokenError (AccessTokenException exception, HttpServletRequest request){
        getLogger().error("{} - Unable to resolve the token", request.getRequestURI(), exception);
        return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(IOException.class)
    public ErrorResponse ioError(final IOException exception, final HttpServletRequest request) {
        getLogger().error("IO Error", exception);

        return createErrorResponse(HttpStatus.NOT_FOUND.value(), AuthConstants.ERROR_IO, request);
    }

}
