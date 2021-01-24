package com.kfyty.netty.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/21 15:18
 * @email kfyty725@hotmail.com
 */
@Slf4j
public abstract class AbstractHeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
        }
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE) {
            this.handleReadTimeout(ctx, event);
        }
        if (event.state() == IdleState.WRITER_IDLE) {
            this.handleWriteTimeout(ctx, event);
        }
        if(event.state() == IdleState.ALL_IDLE) {
            this.handleReadWriteTimeout(ctx, event);
        }
    }

    protected void handleReadTimeout(ChannelHandlerContext ctx, IdleStateEvent evt) {

    }

    protected void handleWriteTimeout(ChannelHandlerContext ctx, IdleStateEvent evt) {

    }

    protected void handleReadWriteTimeout(ChannelHandlerContext ctx, IdleStateEvent evt) {

    }
}
