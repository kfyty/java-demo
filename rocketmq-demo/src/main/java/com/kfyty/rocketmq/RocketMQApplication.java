package com.kfyty.rocketmq;

import com.kfyty.rocketmq.model.MessageModel;
import com.kfyty.rocketmq.producer.BaseProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.kfyty.rocketmq.producer.BaseProducer.ASYNC_GROUP;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/24 16:36
 * @email kfyty725@hotmail.com
 */
@Slf4j
@SpringBootApplication
public class RocketMQApplication implements CommandLineRunner {
    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    @Qualifier(ASYNC_GROUP)
    private DefaultMQProducer asyncGroup;

    @Bean(ASYNC_GROUP)
    public DefaultMQProducer mqProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(ASYNC_GROUP);
        producer.setNamesrvAddr(nameServer);
        return producer;
    }

    public static void main(String[] args) {
        SpringApplication.run(RocketMQApplication.class, args);
    }

    @Override
    public void run(String ... args) throws Exception {
        MessageModel message = new MessageModel(1, "hello spring rocket mq");
        rocketMQTemplate.setProducer(asyncGroup);
        rocketMQTemplate.asyncSend("spring_test_topic:" + BaseProducer.TAG, MessageBuilder.withPayload(message).build(), new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("send success: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("send error !", throwable);
            }
        });
    }

    @Component
    @RocketMQMessageListener(topic = "spring_test_topic", consumerGroup = "default_group")
    private static class Consumer implements RocketMQListener<MessageModel> {

        @Override
        public void onMessage(MessageModel message) {
            log.info("consumer msg: {}", message);
        }
    }
}
