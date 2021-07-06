package com.kfyty.demo.utils;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 描述: 解密工具
 *
 * @author kfyty725
 * @date 2021/7/6 19:40
 * @email kfyty725@hotmail.com
 */
public abstract class DecryptUtil {
    private static final Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptAES128CBC(String encryptData, String sessionKey, String ivParameter) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        SecretKeySpec secretKeySpec = new SecretKeySpec(base64Decoder.decodeBuffer(sessionKey), "AES");
        IvParameterSpec iv = new IvParameterSpec(base64Decoder.decodeBuffer(ivParameter));
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] encrypted = base64Decoder.decodeBuffer(encryptData);
        byte[] result = cipher.doFinal(encrypted);
        return new String(result, StandardCharsets.UTF_8);
    }
}
