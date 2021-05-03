package com.kfyty.redis.handler;

import com.kfyty.redis.delay.listener.handler.RedisExpireHandler;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 12:38
 * @email kfyty725@hotmail.com
 */
@Component("testExpire")
public class TestExpireHandler implements RedisExpireHandler {
    @Override
    public void doHandle(String key) {
        System.out.println(key);
    }
}
