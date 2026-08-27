/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.utils.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String DEFAULT_IV = "DingYangMall2024";

    public static String encrypt(String content, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(1, (Key)keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch (Exception e) {
            throw new RuntimeException("AES\u52a0\u5bc6\u5931\u8d25", e);
        }
    }

    public static String decrypt(String encryptedContent, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(2, (Key)keySpec, ivSpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedContent);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            throw new RuntimeException("AES\u89e3\u5bc6\u5931\u8d25", e);
        }
    }

    public static String encrypt(String content) {
        return AesEncryptUtils.encrypt(content, AesEncryptUtils.getDefaultKey());
    }

    public static String decrypt(String encryptedContent) {
        return AesEncryptUtils.decrypt(encryptedContent, AesEncryptUtils.getDefaultKey());
    }

    private static String getDefaultKey() {
        String key = System.getenv("DINGYANG_AES_KEY");
        if (key == null || key.length() != 16) {
            key = DEFAULT_IV;
        }
        return key;
    }
}

