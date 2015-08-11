package com.tchepannou.auth.rr;

import java.util.Date;

public class AccessTokenResponse {
    //-- Attributes
    private long id;
    private long userId;
    private Date created;

    //-- Getter/Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
