package com.kfyty.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
@Slf4j
@Component
public class RedisUtil {
    private static RedissonClient redissonClient;
    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedisUtil.redissonClient = redissonClient;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static void lock(String key, long timeout, TimeUnit timeUnit) {
        redissonClient.getLock(key).lock(timeout, timeUnit);
    }

    public static boolean tryLock(String key, long timeout, long waitTimeout, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTimeout, timeout, timeUnit);
        } catch (InterruptedException e) {
            log.error("RedisUtil.lock error !", e);
            return false;
        }
    }

    public static boolean unlock(String key) {
        try {
            redissonClient.getLock(key).unlock();
            return true;
        } catch (Exception e) {
            log.error("RedisUtil.unlock error !", e);
            return false;
        }
    }
}
