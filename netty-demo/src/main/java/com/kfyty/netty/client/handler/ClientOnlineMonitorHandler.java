package com.kfyty.netty.client.handler;

import com.kfyty.netty.client.NettyClient;
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
public class ClientOnlineMonitorHandler extends ChannelInboundHandlerAdapter {
    private final NettyClient client;

    private final AtomicBoolean triggerException = new AtomicBoolean(false);

    public ClientOnlineMonitorHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("服务端:{} 已上线！", ctx.channel().remoteAddress());
        this.triggerException.set(false);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("服务端:{} 已下线！", ctx.channel().remoteAddress());
        if(!this.triggerException.get()) {
            this.client.reconnect();
        }
    }

    /**
     * 执行 fireExceptionCaught 方法时可能会触发 fireChannelInactive，从而调用 channelInactive
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端:{} 发生异常：{}！", ctx.channel().remoteAddress(), cause.getMessage());
        if(cause instanceof IOException) {
            this.triggerException.set(true);
            this.client.reconnect();
        }
    }
}
