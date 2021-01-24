package com.kfyty.netty.server.handler;

import com.kfyty.netty.NettyConfig;
import com.kfyty.netty.model.ResponseInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/21 14:22
 * @email kfyty725@hotmail.com
 */
public class StringMessageHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        try {
            System.out.println(msg);
            ctx.writeAndFlush(ResponseInfo.SUCCESS() + NettyConfig.DELIMITER);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
