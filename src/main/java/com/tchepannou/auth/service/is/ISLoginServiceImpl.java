package com.tchepannou.auth.service.is;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.LoginRequest;
import com.tchepannou.auth.exception.AuthenticationException;
import com.tchepannou.auth.service.AccessTokenService;
import com.tchepannou.auth.service.LoginService;
import com.tchepannou.core.http.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public AccessTokenResponse login(LoginRequest request) {
        try {

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
                throw new AuthenticationException(error);
            }

            String accessTokenId = (String)result.get("login_id");
            return accessTokenService.findById(accessTokenId);

        } catch (IOException e) {
            throw new AuthenticationException("connection_error", e);
        }
    }

    @Override
    @Transactional
    public void logout(String accessTokenId) {
        try {

            new Http()
                    .withHost(hostname)
                    .withObjectMapper(jackson.build())
                    .withPath("/is-api-web/login/signout.json")
                    .withParams(Collections.singletonMap("id", accessTokenId))
                    .withPort(port)
                    .get();

        } catch (IOException e) {
            throw new IllegalStateException("Unable to logout", e);
        }
    }

    //-- Private
}
