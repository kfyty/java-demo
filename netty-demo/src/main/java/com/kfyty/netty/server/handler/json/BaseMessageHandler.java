package com.kfyty.netty.server.handler.json;

import com.kfyty.netty.model.BaseMessage;
import com.kfyty.netty.model.ResponseInfo;
import com.kfyty.support.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.kfyty.netty.NettyConfig.DELIMITER;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/8 13:58
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class BaseMessageHandler extends SimpleChannelInboundHandler<BaseMessage> {
    private final Map<String, MessageHandler<?>> messageHandlerMap = new HashMap<String, MessageHandler<?>>() {{
        put("test", new TestMessageHandler());
    }};

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        try {
            MessageHandler<?> messageHandler = messageHandlerMap.get(msg.getMsgType());
            if(messageHandler == null) {
                ctx.writeAndFlush(ResponseInfo.FAILED("未知消息类型") + DELIMITER);
                return;
            }
            messageHandler.handler(ctx, JsonUtil.toObject(msg.getOriginJson(), messageHandler.handlerType()));
        } catch (Exception e) {
            log.error("BaseMessageHandler error !", e);
            ctx.writeAndFlush(ResponseInfo.FAILED(e.getMessage()) + DELIMITER);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
