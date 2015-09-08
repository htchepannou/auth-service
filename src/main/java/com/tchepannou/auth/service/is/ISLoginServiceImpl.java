package com.tchepannou.auth.service.is;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.exception.AuthenticationException;
import com.tchepannou.auth.service.AccessTokenService;
import com.tchepannou.auth.service.LoginService;
import com.tchepannou.core.http.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class ISLoginServiceImpl implements LoginService {
    //-- Attributes
    @Value("${insidesoccer.protocol}")
    private String protocol;

    @Value("${insidesoccer.hostname}")
    private String hostname;

    @Value("${insidesoccer.port}")
    private int port;

    @Autowired
    private Jackson2ObjectMapperBuilder jackson;

    @Autowired
    private AccessTokenService accessTokenService;

    //-- LoginService overrides
    @Override
    public AccessTokenResponse login(LoginRequest request) throws IOException {
        Map result = new Http()
                .withProtocol(protocol)
                .withHost(hostname)
                .withPort(port)
                .withPath("/is-api-web/login/signin.json")
                .param("name", request.getUsername())
                .param("password", request.getPassword())
                .withObjectMapper(jackson.build())
                .get(Map.class);

        String error = (String)result.get("error_code");
        if (!"0".equals(error)){
            throw new AuthenticationException(AuthConstants.ERROR_AUTH_FAILED);
        }

        String accessTokenId = (String)result.get("login_id");
        return accessTokenService.findById(accessTokenId);
    }

    @Override
    public void logout(String accessTokenId) throws IOException {
        new Http()
                .withProtocol(protocol)
                .withHost(hostname)
                .withPort(port)
                .withObjectMapper(jackson.build())
                .withPath("/is-api-web/login/signout.json")
                .withParams(Collections.singletonMap("id", accessTokenId))
                .get();
    }
}
