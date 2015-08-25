package com.tchepannou.auth.service.is;

import com.tchepannou.auth.domain.AccessToken;
import com.tchepannou.auth.exception.AccessTokenException;
import com.tchepannou.auth.exception.AccessTokenExpiredException;
import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ISAccessTokenService implements AccessTokenService {
    //-- Attributes
    @Autowired
    private DataSource dataSource;

    //-- AccessTokenService overrides
    @Override
    public AccessTokenResponse findById(String id) throws AccessTokenException{
        final String sql = "SELECT * FROM login WHERE login_id=?";
        AccessToken token = new JdbcTemplate(dataSource).queryForObject(
                sql,
                new Object[]{Long.parseLong(id)},
                getRowMapper()
        );
        if (token.hasExpired()){
            throw new AccessTokenExpiredException("expired");
        }
        return map(token);
    }


    //-- Private
    private AccessTokenResponse map (AccessToken token){
        AccessTokenResponse response = new AccessTokenResponse();
        response.setCreated(token.getCreated());
        response.setUserId(token.getUserId());
        response.setId(String.valueOf(token.getId()));

        return response;
    }
    private RowMapper<AccessToken> getRowMapper (){
        return new RowMapper<AccessToken>() {
            @Override
            public AccessToken mapRow(ResultSet rs, int i) throws SQLException {
                AccessToken obj = new AccessToken();
                obj.setId(rs.getLong("login_id"));
                obj.setUserId(rs.getLong("login_party_fk"));
                obj.setActive(rs.getBoolean("login_active"));
                obj.setCreated(rs.getTimestamp("login_date"));
                obj.setLogoutDate(rs.getTimestamp("login_logout_date"));
                return obj;
            }
        };
    }
}
