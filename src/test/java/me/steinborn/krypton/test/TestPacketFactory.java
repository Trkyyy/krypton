// File: src/test/java/me/steinborn/krypton/test/TestPacketFactory.java
package me.steinborn.krypton.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.steinborn.krypton.mod.shared.network.pipeline.MinecraftCipherDecoder;
import me.steinborn.krypton.mod.shared.network.pipeline.MinecraftCipherEncoder;
import me.steinborn.krypton.mod.shared.network.pipeline.MinecraftVarintPrepender;
import net.minecraft.network.VarInt;

/**
 * Factory for creating test packets and pipeline components.
 */
public class TestPacketFactory {
    
    /**
     * Creates a test ByteBuf with Minecraft-style framing.
     */
    public static ByteBuf createMinecraftPacket(byte[] data) {
        ByteBuf buffer = Unpooled.buffer();
        VarInt.write(buffer, data.length);
        buffer.writeBytes(data);
        return buffer;
    }
    
    /**
     * Creates a test ByteBuf with multiple Minecraft packets.
     */
    public static ByteBuf createMultiPacketData(byte[]... packets) {
        ByteBuf buffer = Unpooled.buffer();
        for (byte[] packet : packets) {
            VarInt.write(buffer, packet.length);
            buffer.writeBytes(packet);
        }
        return buffer;
    }
    
    /**
     * Creates encryption/decryption handler pair for testing.
     */
    public static EncryptionHandlerPair createEncryptionHandlers(SecretKey key) throws GeneralSecurityException {
        VelocityCipher decryption = Natives.cipher.get().forDecryption(key);
        VelocityCipher encryption = Natives.cipher.get().forEncryption(key);
        
        return new EncryptionHandlerPair(
            new MinecraftCipherDecoder(decryption),
            new MinecraftCipherEncoder(encryption)
        );
    }
    
    /**
     * Holds a pair of encryption and decryption handlers.
     */
    public static class EncryptionHandlerPair {
        private final MinecraftCipherDecoder decoder;
        private final MinecraftCipherEncoder encoder;
        
        public EncryptionHandlerPair(MinecraftCipherDecoder decoder, MinecraftCipherEncoder encoder) {
            this.decoder = decoder;
            this.encoder = encoder;
        }
        
        public MinecraftCipherDecoder getDecoder() { return decoder; }
        public MinecraftCipherEncoder getEncoder() { return encoder; }
    }
}