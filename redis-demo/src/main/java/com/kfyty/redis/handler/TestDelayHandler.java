package com.kfyty.redis.handler;

import com.kfyty.redis.delay.handler.RedisDelayHandler;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 12:38
 * @email kfyty725@hotmail.com
 */
@Component("test")
public class TestDelayHandler implements RedisDelayHandler {

    @Override
    public long timeout() {
        return 5 * 1000;
    }

    @Override
    public long lockTimeout() {
        return 5 * 1000;
    }

    @Override
    public boolean doHandle(String data) {
        System.out.println(data);
        return true;
    }
}
