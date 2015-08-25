package com.tchepannou.auth.controller;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.service.LoginService;
import com.tchepannou.core.http.Http;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(basePath = "/v1/login", value = "Login/Logout", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController extends AbstractController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value="Logs a user in")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 409, message = "Authentication failed")
    })
    public AccessTokenResponse login (@Valid @RequestBody LoginRequest request){
        return loginService.login(request);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value="Logs a user out")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 404, message = "Access token not found")
    })
    public void logout (@RequestParam(Http.HEADER_ACCESS_TOKEN) String accessToken){
        loginService.logout(accessToken);
    }
}
