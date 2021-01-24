package com.kfyty.netty.server.handler.json;

import com.kfyty.netty.model.BaseMessage;
import com.kfyty.netty.model.ResponseInfo;
import io.netty.channel.ChannelHandlerContext;

import static com.kfyty.netty.NettyConfig.DELIMITER;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/9 10:58
 * @email kfyty725@hotmail.com
 */
public interface MessageHandler<T extends BaseMessage> {

    Class<T> handlerType();

    void doHandler(ChannelHandlerContext ctx, T msg);

    default void handler(ChannelHandlerContext ctx, BaseMessage message) {
        this.doHandler(ctx, (T) message);
    }

    default void success(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(ResponseInfo.SUCCESS() + DELIMITER);
    }
}
