// File: src/test/java/me/steinborn/krypton/test/TestConstants.java
package me.steinborn.krypton.test;

/**
 * Constants used across integration tests.
 */
public class TestConstants {
    // Common Minecraft packet sizes for testing
    public static final int TINY_PACKET = 1;
    public static final int SMALL_PACKET = 64;
    public static final int MEDIUM_PACKET = 256;
    public static final int LARGE_PACKET = 1024;
    public static final int CHUNK_PACKET = 4096;
    public static final int MAX_PACKET = 2 * 1024 * 1024; // 2MB - Minecraft's max packet size
    
    // Performance test parameters
    public static final int PERFORMANCE_ITERATIONS = 1000;
    public static final int BENCHMARK_WARMUP = 100;
    
    private TestConstants() {}
}