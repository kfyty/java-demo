package com.kfyty.shiro.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

public abstract class EncryptionUtil {
    public static String encryption(String s) {
        Assert.notNull(s, "encryption string can't null !");
        return new Md5Hash(s).toHex();
    }

    public static String encryption(String s, String salt, int iter) {
        Assert.notNull(s, "encryption string can't null !");
        Assert.notNull(salt, "encryption string salt can't null !");
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        return new Md5Hash(bytes, ByteSource.Util.bytes(salt), iter).toHex();
    }
}
