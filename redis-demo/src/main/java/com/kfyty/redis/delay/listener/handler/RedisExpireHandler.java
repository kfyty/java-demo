package com.kfyty.redis.delay.listener.handler;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 12:38
 * @email kfyty725@hotmail.com
 */
public interface RedisExpireHandler {
    void doHandle(String key);
}
