package com.tchepannou.auth.domain;

import junit.framework.TestCase;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenTest extends TestCase {

    public void testHasExpired_ByActive() throws Exception {
        AccessToken token = new AccessToken();
        token.setActive(false);
        token.setLogoutDate(null);

        assertThat(token.hasExpired()).isTrue ();
    }

    public void testHasExpired_ByDate() throws Exception {
        AccessToken token = new AccessToken();
        token.setActive(true);
        token.setLogoutDate(new Date());

        assertThat(token.hasExpired()).isTrue ();
    }
}
