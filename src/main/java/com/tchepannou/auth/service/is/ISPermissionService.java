package com.tchepannou.auth.service.is;

import com.tchepannou.auth.client.v1.PermissionCollectionResponse;
import com.tchepannou.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ISPermissionService implements PermissionService {
    //-- Attributes
    @Autowired
    private DataSource dataSource;

    //-- PermissionService overrides
    @Override
    public PermissionCollectionResponse findByUserBySpaceByAppication(long userId, long spaceId, String app) {
        final PermissionCollectionResponse response = new PermissionCollectionResponse();
        response.setUserId(userId);
        response.setSpaceId(spaceId);
        response.setApplication(app);

        final String sql = "SELECT perm_value\n"
                + "FROM permission P \n"
                + "\tJOIN application A ON P.perm_app_fk=A.app_id\n"
                + "\tJOIN prel R ON P.perm_role_fk=R.prel_qualifier\n"
                + "WHERE\n"
                + "\tA.app_name=?\n"
                + "\tAND prel_type_fk=10\n"
                + "\tAND prel_source_fk=?\n"
                + "\tAND prel_dest_fk=?\n"
                + ";";
        new JdbcTemplate(dataSource).query(
                sql,
                new Object[] {app, userId, spaceId},
                (rs, i) -> {
                    response.addPermission(String.format("%s-%s", app, rs.getString("perm_value")));
                    return response;
                }
        );

        return response;
    }
}
