package com.kfyty.rocketmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 描述: 同步消息生产者
 *
 * @author kfyty
 * @date 2020/11/24 10:07
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class SyncProducer extends BaseProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(SYNC_GROUP);
        producer.setNamesrvAddr(ADDRESS);
        producer.setVipChannelEnabled(false);
        producer.start();
        sendMsg(producer);
        producer.shutdown();
    }

    private static void sendMsg(DefaultMQProducer producer) throws Exception {
        int i = 0;
        while (true) {
            Message message = new Message(TOPIC, TAG, ("hello rocketmq " + i++).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult result = producer.send(message);
            if(result.getSendStatus() != SendStatus.SEND_OK) {
                break;
            }
//            producer.sendOneway(message);                   // 单向发送
            log.info("send result: {}", result);
        }
    }
}
