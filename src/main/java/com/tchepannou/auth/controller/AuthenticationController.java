package com.tchepannou.auth.controller;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.exception.AuthenticationException;
import com.tchepannou.auth.service.LoginCommand;
import com.tchepannou.auth.service.LoginService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;

@RestController
@Api(basePath = "/v1/auth", value = "Login/Logout", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController extends AbstractController {
    @Autowired
    private LoginCommand loginCommand;

    @Autowired
    private LoginService loginService;

    //-- REST endpoints
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ApiOperation(value="Logs a user in")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 409, message = AuthConstants.ERROR_AUTH_FAILED),
            @ApiResponse(code = 409, message = AuthConstants.ERROR_IO)
    })
    public AccessTokenResponse login (
            @RequestHeader(Http.HEADER_TRANSACTION_ID) String transactionId,
            @Valid @RequestBody LoginRequest request
    ) throws IOException {
        return loginCommand.execute(
                request,
                new CommandContextImpl().withTransactionId(transactionId)
        );
    }

    @RequestMapping(method = RequestMethod.POST, value="/logout/{accessToken}")
    @ApiOperation(value="Logs a user out")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 404, message = "Access token not found")
    })
    public void logout (@RequestParam String accessToken) throws IOException {
        loginService.logout(accessToken);
    }


    //-- Error handler
    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse authFailed(final Exception exception, final HttpServletRequest request) {
        getLogger().error("Authentication failed", exception);

        return createErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(IOException.class)
    public ErrorResponse ioError(final IOException exception, final HttpServletRequest request) {
        getLogger().error("IO Error", exception);

        return createErrorResponse(HttpStatus.CONFLICT.value(), AuthConstants.ERROR_IO, request);
    }

}
