package com.tchepannou.auth.service;

import com.tchepannou.auth.client.v1.PermissionCollectionResponse;

public interface PermissionService {
    PermissionCollectionResponse findByUserBySpaceByAppication(long userId, long spaceId, String app);
}
