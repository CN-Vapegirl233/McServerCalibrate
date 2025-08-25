package com.example.minecraftplugin;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    // 使用AES加密算法
    private static final String ALGORITHM = "AES";
    // 解密密码
    private static final String SECRET_KEY = "vapegirl233";

    // 加密方法
    public static String encrypt(String value) {
        try {
            // 使用16字节密钥 (AES-128)
            byte[] keyBytes = new byte[16];
            byte[] secretKeyBytes = SECRET_KEY.getBytes();
            System.arraycopy(secretKeyBytes, 0, keyBytes, 0, Math.min(secretKeyBytes.length, keyBytes.length));

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密方法
    public static String decrypt(String encryptedValue) {
        try {
            // 使用16字节密钥 (AES-128)
            byte[] keyBytes = new byte[16];
            byte[] secretKeyBytes = SECRET_KEY.getBytes();
            System.arraycopy(secretKeyBytes, 0, keyBytes, 0, Math.min(secretKeyBytes.length, keyBytes.length));

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}