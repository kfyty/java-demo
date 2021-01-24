package com.kfyty.netty.server.handler.json;

import com.kfyty.netty.model.TestMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/9 11:00
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class TestMessageHandler implements MessageHandler<TestMessage> {
    @Override
    public Class<TestMessage> handlerType() {
        return TestMessage.class;
    }

    @Override
    public void doHandler(ChannelHandlerContext ctx, TestMessage msg) {
        log.info("TestMessageHandler:{}", msg);
        this.success(ctx);
    }
}
