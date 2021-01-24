package com.kfyty.redis.delay.handler;

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
     * @return 毫秒数
     */
    long lockTimeout();

    /**
     * 处理任务
     * 返回 true 时表示处理成功，会从 redis 中删除任务
     * 返回 false 时表示处理失败，将不会从 redis 中删除任务，而是等待下一次继续处理
     * @param data 数据
     * @return true/false
     */
    boolean doHandle(String data);
}
