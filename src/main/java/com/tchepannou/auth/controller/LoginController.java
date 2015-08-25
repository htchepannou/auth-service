package com.tchepannou.auth.controller;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(basePath = "/v1/login", value = "Login/Logout", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController extends AbstractController {

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value="Logs a user in")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 409, message = "Authentication failed")
    })
    public AccessTokenResponse login (){
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value="Logs a user out")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 404, message = "Access token not found")
    })
    public void logout (@RequestParam("access_token") String accessToken){
    }
}
