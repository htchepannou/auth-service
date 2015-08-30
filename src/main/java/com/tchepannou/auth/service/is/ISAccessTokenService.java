package com.tchepannou.auth.service.is;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.auth.exception.AccessTokenException;
import com.tchepannou.auth.service.AccessTokenService;
import com.tchepannou.core.http.Http;
import com.tchepannou.core.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ISAccessTokenService implements AccessTokenService {
    //-- Attributes
    @Value("${insidesoccer.protocol}")
    private String protocol;

    @Value("${insidesoccer.hostname}")
    private String hostname;

    @Value("${insidesoccer.port}")
    private int port;

    @Autowired
    private Jackson2ObjectMapperBuilder jackson;


    //-- AccessTokenService overrides
    @Override
    public AccessTokenResponse findById(String id) throws IOException {
        try {

            Map result = new Http()
                    .withProtocol(protocol)
                    .withHost(hostname)
                    .withPort(port)
                    .withPath("/is-api-web/login/show.json")
                    .param("id", id)
                    .withObjectMapper(jackson.build())
                    .get(Map.class);

            return map(result);

        } catch (ParseException e){
            throw new AccessTokenException(AuthConstants.ERROR_BAD_TOKEN, e);
        } catch (HttpException e){
            if (e.getStatus() == 404) {
                throw new AccessTokenException(AuthConstants.ERROR_TOKEN_NOT_FOUND, e);
            }
            throw e;
        }
    }


    //-- Private
    private AccessTokenResponse map (Map result) throws ParseException {
        if (Boolean.FALSE.equals(result.get("active")) || result.get("logout_date") != null){
            throw new AccessTokenException(AuthConstants.ERROR_TOKEN_EXPIRED);
        }

        final AccessTokenResponse response = new AccessTokenResponse();

        response.setId((String) result.get("id"));

        String userId = (String)((Map)result.get("user")).get("id");
        response.setUserId(Long.parseLong(userId));

        final SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
        response.setCreated(fmt.parse((String)result.get("date")));

        return response;
    }
}
