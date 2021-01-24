package com.kfyty.rocketmq.producer;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/24 10:11
 * @email kfyty725@hotmail.com
 */
public abstract class BaseProducer {
    public static final String ADDRESS = "www.kfyty.com:6364";

    public static final String SYNC_GROUP = "sync_group";

    public static final String ASYNC_GROUP = "async_group";

    public static final String TOPIC = "test_topic";

    public static final String TAG = "test_tag";
}
