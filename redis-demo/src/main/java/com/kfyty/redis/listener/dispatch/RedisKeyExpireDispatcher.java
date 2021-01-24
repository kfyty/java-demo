package com.kfyty.redis.listener.dispatch;

import com.kfyty.redis.listener.handler.RedisExpireHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 11:51
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Component
public class RedisKeyExpireDispatcher extends KeyExpirationEventMessageListener {
    private Map<String, RedisExpireHandler> redisExpireHandler;

    @Autowired
    public void initialize(Map<String, RedisExpireHandler> redisExpireHandler) {
        this.redisExpireHandler = redisExpireHandler;
    }

    public RedisKeyExpireDispatcher(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doHandleMessage(Message message) {
        String key = message.toString();
        for (Map.Entry<String, RedisExpireHandler> entry : redisExpireHandler.entrySet()) {
            if(key.startsWith(entry.getKey())) {
                entry.getValue().doHandle(key);
                return;
            }
        }
        log.error("No redis expire handler found for key prefix: {} !", key);
    }
}
