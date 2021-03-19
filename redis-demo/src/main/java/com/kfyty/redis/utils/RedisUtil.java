package com.kfyty.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/13 19:06
 * @email kfyty725@hotmail.com
 */
@Component
public class RedisUtil {
    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static boolean lock(String key, String value, long timeout) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
        return result != null && result;
    }

    public static boolean unlock(String key) {
        Boolean result = redisTemplate.delete(key);
        return result != null && result;
    }
}
