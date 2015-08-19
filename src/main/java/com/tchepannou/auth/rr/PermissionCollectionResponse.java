package com.tchepannou.auth.rr;

import java.util.HashSet;
import java.util.Set;

public class PermissionCollectionResponse {
    //-- Attributes
    private long userId;
    private long spaceId;
    private String application;
    private Set<String> permissions = new HashSet<>();

    public void addPermission (String permission){
        permissions.add(permission);
    }
    //-- Getter/Setter
    public Set<String> getPermissions() {
        return permissions;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(long spaceId) {
        this.spaceId = spaceId;
    }
}
