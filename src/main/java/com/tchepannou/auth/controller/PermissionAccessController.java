package com.tchepannou.auth.controller;

import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.auth.client.v1.PermissionCollectionResponse;
import com.tchepannou.auth.service.PermissionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(basePath = "/v1/permission", value = "Permission", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/permission", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionAccessController  extends AbstractController{
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET, value="/user/{uid}/space/{sid}/app/{app}")
    @ApiOperation(value="Get permissions by application")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccessTokenResponse.class),
            @ApiResponse(code = 401, message = "Access token has expired")
    })
    public PermissionCollectionResponse getByUserBySpaceByAppication(
            @PathVariable("uid") long userId,
            @PathVariable("sid") long spaceId,
            @PathVariable("app") String app
    ){
        return permissionService.findByUserBySpaceByAppication(userId, spaceId, app);
    }
}
