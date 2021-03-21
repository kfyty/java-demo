package com.kfyty.redis.delay.handler;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/13 10:42
 * @email kfyty725@hotmail.com
 */
public interface RedisDelayHandler {
    /**
     * 过期时间
     * @return 毫秒数
     */
    long timeout();

    /**
     * 锁过期时间
     * @return 过期时间
     */
    long lockTimeout();

    /**
     * 获取锁等待时间
     * @return 堵塞等待时间
     */
    default long tryWaitTimeout() {
        return 3000L;
    }

    /**
     * 超时时间单位
     * @return 时间单位
     */
    default TimeUnit timeoutUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * 处理任务
     * 返回 true 时表示处理成功，会从 redis 中删除任务
     * 返回 false 时表示处理失败，将不会从 redis 中删除任务，而是等待下一次继续处理
     * @param data 数据
     * @return true/false
     */
    boolean doHandle(String data);
}
