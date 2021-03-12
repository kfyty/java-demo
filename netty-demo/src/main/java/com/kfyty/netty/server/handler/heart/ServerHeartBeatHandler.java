package com.kfyty.netty.server.handler.heart;

import com.kfyty.netty.NettyConfig;
import com.kfyty.netty.common.handler.AbstractHeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import static com.kfyty.netty.NettyConfig.DELIMITER;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/21 14:40
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class ServerHeartBeatHandler extends AbstractHeartBeatHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!NettyConfig.PINT.equals(msg)) {
            super.channelRead(ctx, msg);
            return;
        }
        ctx.channel().writeAndFlush(NettyConfig.PONT + DELIMITER);      // 从当前处理器开始回写
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void handleReadWriteTimeout(ChannelHandlerContext ctx, IdleStateEvent evt) {
        log.info("未收到客户端心跳数据，断开连接: {}！", ctx.channel().remoteAddress());
        ctx.channel().close();
    }
}
