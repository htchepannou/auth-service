package com.tchepannou.auth.service.impl;

import com.tchepannou.auth.service.PasswordEncryptor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncryptorImplTest {
    private final PasswordEncryptor service = new PasswordEncryptorImpl();

    @Test
    public void test_encrypt_null() throws Exception {
        assertThat(service.encrypt(null)).isNull();
    }

    @Test
    public void test_matches() throws Exception {
        String xpassword = service.encrypt("secret");

        assertThat(service.matches("secret", xpassword)).isTrue();
    }

    @Test
    public void test_matches_null() throws Exception {
        String xpassword = service.encrypt("secret");

        assertThat(service.matches(null, xpassword)).isFalse();
    }


    @Test
    public void test_matches_all_null() throws Exception {
        assertThat(service.matches(null, null)).isTrue();
    }
}
