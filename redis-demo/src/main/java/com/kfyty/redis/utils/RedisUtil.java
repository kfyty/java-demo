package com.kfyty.redis.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/13 19:06
 * @email kfyty725@hotmail.com
 */
@Component
public class RedisUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private static JedisPool jedisPool;

    public static boolean lock(String key, String value, long timeout) {
        try(Jedis jedis = jedisPool.getResource()) {
            return "OK".equals(jedis.set(key, value, "nx", "px", timeout));
        }
    }

    public static boolean unlock(String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            Long count = jedis.del(key);
            return count != null && count == 1;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisUtil.applicationContext = applicationContext;
        RedisUtil.jedisPool = applicationContext.getBean(JedisPool.class);
    }
}
