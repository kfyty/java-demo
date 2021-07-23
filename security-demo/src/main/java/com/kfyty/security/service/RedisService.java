package com.kfyty.security.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 12:04
 * @email kfyty725@hotmail.com
 */
@Service
public class RedisService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void delete(String key) {
        this.redisTemplate.delete(key);
    }

    public void expire(String key) {
        this.expire(key, 180);
    }

    public void expire(String key, long time) {
        this.expire(key, time, TimeUnit.SECONDS);
    }

    public void expire(String key, long time, TimeUnit timeUnit) {
        this.redisTemplate.expire(key, time, timeUnit);
    }

    public boolean exists(String key) {
        Boolean hasKey = this.redisTemplate.hasKey(key);
        return hasKey != null && hasKey;
    }

    /**
     * 将一个对象以 hash 形式保存
     */
    public void hset(String key, Object o) {
        this.hset(key, o, -1, null);
    }

    /**
     * 将一个 hash 以对象形式返回
     */
    public <T> T hget(String key, Class<T> clazz) {
        Map<Object, Object> map = this.redisTemplate.opsForHash().entries(key);
        return CollectionUtils.isEmpty(map) ? null : this.objectMapper.convertValue(map, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T hget(String key, String field) {
        return (T) this.redisTemplate.opsForHash().get(key, field);
    }

    public void hset(String key, Object o, long time, TimeUnit timeUnit) {
        Map<String, Object> map = this.objectMapper.convertValue(o, new TypeReference<HashMap<String, Object>>() {}).entrySet().stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.redisTemplate.opsForHash().putAll(key, map);
        if(time > -1) {
            this.expire(key, time, timeUnit);
        }
    }
}
