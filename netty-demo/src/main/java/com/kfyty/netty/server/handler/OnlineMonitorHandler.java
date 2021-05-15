package com.kfyty.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/21 14:28
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class OnlineMonitorHandler extends ChannelInboundHandlerAdapter {
    private final AtomicBoolean triggerException = new AtomicBoolean(false);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端:{} 已连接！", ctx.channel().remoteAddress());
        this.triggerException.set(false);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
        log.info("客户端:{} 已断开连接！", ctx.channel().remoteAddress());
        if(!this.triggerException.get()) {
            // do something
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端:{} 发生异常：{}！", ctx.channel().remoteAddress(), cause.getMessage());
        if(cause instanceof IOException) {
            this.triggerException.set(true);
            ctx.channel().close();
        }
    }
}
