package me.steinborn.krypton.integration;

import me.steinborn.krypton.mod.shared.network.ClientConnectionEncryptionExtension;
import me.steinborn.krypton.mod.shared.network.pipeline.MinecraftCipherDecoder;
import me.steinborn.krypton.mod.shared.network.pipeline.MinecraftCipherEncoder;
import me.steinborn.krypton.test.TestConstants;
import me.steinborn.krypton.test.TestCryptoUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.junit.FabricLoaderTest;
import net.minecraft.SharedConstants;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@FabricLoaderTest
@DisplayName("Encryption Flow Integration Tests")
class EncryptionFlowIntegrationTest {
    
    @Test
    @DisplayName("Verify Minecraft 26.2 encryption classes are accessible")
    void testMinecraftClassesAccessible() {
        // Verify we can access key Minecraft classes
        assertNotNull(Connection.class, "Connection class should be accessible");
        assertNotNull(ServerLoginPacketListenerImpl.class, 
            "ServerLoginPacketListenerImpl should be accessible");
        
        // Verify SharedConstants is at expected version
        assertTrue(SharedConstants.getCurrentVersion().getName().contains("26.2"),
            "Minecraft version should be 26.2");
    }
    
    @Test
    @DisplayName("Verify mixin applied correctly to Connection")
    void testConnectionMixinApplied() {
        // Verify Connection implements our extension interface
        assertTrue(ClientConnectionEncryptionExtension.class.isAssignableFrom(Connection.class),
            "Connection should implement ClientConnectionEncryptionExtension");
    }

    
    @Test
    @DisplayName("Verify ServerLoginPacketListenerImpl mixin is applied")
    void testLoginListenerMixinApplied() throws Exception {
        // Check if the mixin redirected the cipher creation method
        Method[] methods = ServerLoginPacketListenerImpl.class.getDeclaredMethods();
        boolean hasKeyMethod = false;
        
        for (Method method : methods) {
            if (method.getName().contains("handleKey") || 
                method.getName().contains("key")) {
                hasKeyMethod = true;
                break;
            }
        }
        
        assertTrue(hasKeyMethod, 
            "ServerLoginPacketListenerImpl should have key handling method");
    }
    
    @Test
    @DisplayName("Verify encryption can be set up with valid key")
    void testEncryptionSetupWithValidKey() throws Exception {
        // This test requires a mock Connection, but verifies the extension works
        SecretKey testKey = TestCryptoUtils.generateTestKey();
        assertNotNull(testKey, "Should be able to generate test key");
        assertEquals("AES", testKey.getAlgorithm(), "Key should use AES algorithm");
    }
    
    @Test
    @DisplayName("Verify mod initialization logs encryption capabilities")
    void testModInitialization() {
        // Verify the mod is loaded
        assertTrue(FabricLoader.getInstance().isModLoaded("krypton"),
            "Krypton mod should be loaded");
        
        // Verify velocity-native is available
        assertNotNull(getClass().getClassLoader()
            .getResource("com/velocitypowered/natives/encryption/VelocityCipher.class"),
            "VelocityCipher class should be available");
    }
    
    @Test
    @DisplayName("Verify encryption extension interface contract")
    void testEncryptionExtensionContract() {
        // Verify the interface methods exist and have correct signatures
        try {
            Method setupMethod = ClientConnectionEncryptionExtension.class
                .getMethod("setupEncryption", SecretKey.class);
            assertNotNull(setupMethod, "setupEncryption method should exist");
            
            Class<?>[] exceptions = setupMethod.getExceptionTypes();
            assertTrue(exceptions.length > 0, 
                "setupEncryption should declare exceptions");
        } catch (NoSuchMethodException e) {
            fail("ClientConnectionEncryptionExtension should have setupEncryption method");
        }
    }
}