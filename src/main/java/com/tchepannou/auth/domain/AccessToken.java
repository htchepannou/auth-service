package com.tchepannou.auth.domain;

import java.util.Date;

public class AccessToken {
    //-- Attributes
    private long id;
    private long userId;
    private boolean active;
    private Date created;
    private Date logoutDate;

    //-- Getter/Setter
    public boolean hasExpired (){
        return !active || logoutDate != null;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }
}
