package com.kfyty.rocketmq.consumer;

import com.kfyty.rocketmq.producer.BaseProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/24 11:33
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class NormalConsumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(BaseProducer.ASYNC_GROUP);
        consumer.setNamesrvAddr(BaseProducer.ADDRESS);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.subscribe(BaseProducer.TOPIC, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                log.info("{} consumer message: {}", Thread.currentThread().getName(), new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("consumer start !");
    }
}
