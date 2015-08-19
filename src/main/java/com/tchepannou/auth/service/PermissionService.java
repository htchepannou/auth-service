package com.tchepannou.auth.service;

import com.tchepannou.auth.rr.PermissionCollectionResponse;

public interface PermissionService {
    PermissionCollectionResponse findByUserBySpaceByAppication(long userId, long spaceId, String app);
}
