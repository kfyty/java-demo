package com.kfyty.netty.client.handler.heart;

import com.kfyty.netty.NettyConfig;
import com.kfyty.netty.common.handler.AbstractHeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/21 14:40
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class ClientHeartBeatHandler extends AbstractHeartBeatHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!NettyConfig.PONT.equals(msg)) {
            super.channelRead(ctx, msg);
            return;
        }
        log.info("收到服务器心跳响应！");
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void handleReadTimeout(ChannelHandlerContext ctx, IdleStateEvent evt) {
        log.info("未收到服务器心跳响应，断开连接: {}！", ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    @Override
    protected void handleWriteTimeout(ChannelHandlerContext ctx, IdleStateEvent evt) {
        log.info("发送心跳包...");
        ctx.writeAndFlush(NettyConfig.PINT + NettyConfig.DELIMITER);
    }
}
