package com.tchepannou.auth.service.impl;

import com.tchepannou.auth.service.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class PasswordEncryptorImpl implements PasswordEncryptor {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(PasswordEncryptorImpl.class);
    private static final String HASH_ALGO = "MD5";

    //-- PasswordEncryptor overrides
    @Override
    public String encrypt(final String password) {
        try {
            return password != null ? hash(password, null) : null;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Encryption algorithm not supported", e);
        }
    }

    @Override
    public boolean matches(final String plainText, final String storedHashedPassword) {
        try {
            if (storedHashedPassword == null) {
                return plainText == null;
            }
            if (plainText == null) {
                return storedHashedPassword == null;
            }

            byte[] hashWithSaltBytes = Base64.getDecoder().decode(storedHashedPassword.getBytes());

            // We must know size of computeHash (without salt).
            final int hashSizeInBytes = 16;

            // Make sure that the specified computeHash value is long enough.
            if (hashWithSaltBytes.length < hashSizeInBytes) {
                return false;
            }

            // Allocate array to hold original salt bytes retrieved from computeHash.
            byte[] saltBytes = new byte[hashWithSaltBytes.length - hashSizeInBytes];

            // Copy salt from the end of the computeHash to the new array.
            for (int i = 0; i < saltBytes.length; i++) {
                saltBytes[i] = hashWithSaltBytes[hashSizeInBytes + i];
            }

            // Compute a new computeHash string.
            String expectedHashString = hash(plainText, saltBytes);

            // If the computed computeHash matches the specified computeHash,
            // the plain text value must be correct.
            return storedHashedPassword.equals(expectedHashString);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn(HASH_ALGO + " not supported", e);
            return false;
        }
    }

    //-- Private
    private String hash(final String plainText, final byte[] salt)
            throws NoSuchAlgorithmException {
        byte[] saltBytes = salt;
        if (saltBytes == null) {
            int saltSize = new Random().nextInt(5) + 4;
            saltBytes = new byte[saltSize];

            SecureRandom secRandom = new SecureRandom();
            secRandom.nextBytes(saltBytes);
        }

        byte[] plainTextBytes = plainText.getBytes();
        byte[] plainTextWithSaltBytes = new byte[saltBytes.length + plainTextBytes.length];

        // Copy plain text bytes into resulting array.
        for (int i = 0; i < plainTextBytes.length; i++) {
            plainTextWithSaltBytes[i] = plainTextBytes[i];
        }

        // Append salt bytes to the resulting array.
        for (int i = 0; i < saltBytes.length; i++) {
            plainTextWithSaltBytes[plainTextBytes.length + i] = saltBytes[i];
        }

        MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
        md.reset();
        md.update(plainTextWithSaltBytes);
        plainTextWithSaltBytes = md.digest();

        // Create array which will hold computeHash and original salt bytes.
        byte[] hashWithSaltBytes = new byte[plainTextWithSaltBytes.length + saltBytes.length];

        // Copy computeHash bytes into resulting array.
        for (int i = 0; i < plainTextWithSaltBytes.length; i++) {
            hashWithSaltBytes[i] = plainTextWithSaltBytes[i];
        }

        // Append salt bytes to the result.
        for (int i = 0; i < saltBytes.length; i++) {
            hashWithSaltBytes[plainTextWithSaltBytes.length + i] = saltBytes[i];
        }

        // Convert result into a base64-encoded string.
        return new String(Base64.getEncoder().encode(hashWithSaltBytes));
    }

}
