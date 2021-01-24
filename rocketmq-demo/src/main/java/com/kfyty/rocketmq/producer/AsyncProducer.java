package com.kfyty.rocketmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.TimeUnit;

/**
 * 描述: 异步消息生产者
 *
 * @author kfyty
 * @date 2020/11/24 10:07
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class AsyncProducer extends BaseProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(ASYNC_GROUP);
        producer.setNamesrvAddr(ADDRESS);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        int count = 100;
        CountDownLatch2 latch = new CountDownLatch2(count);
        for (int i = 0; i < count; i++) {
            Message message = new Message(TOPIC, TAG, ("hello async: " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("send success: {}", sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    log.error("send failed !", e);
                }
            });
        }
        latch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }
}
