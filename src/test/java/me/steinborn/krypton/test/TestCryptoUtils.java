// File: src/test/java/me/steinborn/krypton/test/TestCryptoUtils.java
package me.steinborn.krypton.test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for generating test cryptographic materials.
 */
public class TestCryptoUtils {
    
    /**
     * Generates a test AES key suitable for Minecraft encryption testing.
     */
    public static SecretKey generateTestKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // Minecraft uses 128-bit AES
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES algorithm not available", e);
        }
    }
    
    /**
     * Generates test data of specified size filled with deterministic pattern.
     */
    public static byte[] generateTestData(int size) {
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) (i % 256);
        }
        return data;
    }
}