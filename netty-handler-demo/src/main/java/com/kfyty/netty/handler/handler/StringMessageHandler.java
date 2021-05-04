package com.kfyty.netty.handler.handler;

import com.kfyty.netty.handler.config.NettyConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
        System.out.println(msg);
        ctx.writeAndFlush("不支持的消息协议！" + NettyConfig.DELIMITER);
    }
}
