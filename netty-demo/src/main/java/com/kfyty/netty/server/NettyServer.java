package com.kfyty.netty.server;

import com.kfyty.netty.NettyConfig;
import com.kfyty.netty.server.codec.MessageModelDecoder;
import com.kfyty.netty.server.handler.OnlineMonitorHandler;
import com.kfyty.netty.server.handler.StringMessageHandler;
import com.kfyty.netty.server.handler.heart.ServerHeartBeatHandler;
import com.kfyty.netty.server.handler.json.BaseMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static com.kfyty.netty.NettyConfig.DELIMITER;
import static com.kfyty.netty.NettyConfig.PORT;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/8 11:09
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class NettyServer {
    private final NioEventLoopGroup serverGroup;
    private final NioEventLoopGroup clientGroup;
    private final ServerBootstrap serverBootstrap;

    public NettyServer() {
        this.serverGroup = new NioEventLoopGroup();
        this.clientGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    public void start() throws InterruptedException {
        ChannelFuture future = serverBootstrap.group(serverGroup, clientGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_SNDBUF, NettyConfig.DEFAULT_BUF)       // 发送缓冲区
                .childOption(ChannelOption.SO_RCVBUF, NettyConfig.DEFAULT_BUF)       // 接收缓冲区
                .childOption(ChannelOption.SO_KEEPALIVE, true)                  // 开启心跳检测
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(NettyConfig.DEFAULT_BUF, Unpooled.copiedBuffer(DELIMITER.getBytes(StandardCharsets.UTF_8))));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new IdleStateHandler(NettyConfig.READ_TIME_OUT, 0, NettyConfig.SERVER_READ_WRITE_TIME_OUT));
                        ch.pipeline().addLast(new ServerHeartBeatHandler());
                        ch.pipeline().addLast(new MessageModelDecoder());
                        ch.pipeline().addLast(new OnlineMonitorHandler());
                        ch.pipeline().addLast(new BaseMessageHandler());
                        ch.pipeline().addLast(new StringMessageHandler());
                        log.info("IP:{} connected !", ch.remoteAddress());
                    }
                })
                .bind(PORT)
                .sync();
        try {
            future.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
            serverGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().start();
    }
}
