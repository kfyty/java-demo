package com.kfyty.netty.client;

import com.kfyty.netty.NettyConfig;
import com.kfyty.netty.client.handler.ClientMessageHandler;
import com.kfyty.netty.client.handler.ClientOnlineMonitorHandler;
import com.kfyty.netty.client.handler.heart.ClientHeartBeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.kfyty.netty.NettyConfig.DELIMITER;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/8 14:11
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class NettyClient {
    @Getter
    private final NioEventLoopGroup client;
    private final Bootstrap bootstrap;

    @Getter
    private ChannelFuture future;

    public NettyClient() {
        this.client = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    private void init() {
        final NettyClient self = this;
        this.bootstrap.group(client)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_SNDBUF, NettyConfig.DEFAULT_BUF)     // 发送缓冲区
                .option(ChannelOption.SO_RCVBUF, NettyConfig.DEFAULT_BUF)     // 接收缓冲区
                .option(ChannelOption.SO_KEEPALIVE, true)       // 开启心跳检测
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(NettyConfig.DEFAULT_BUF, Unpooled.copiedBuffer(DELIMITER.getBytes(StandardCharsets.UTF_8))));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new IdleStateHandler(0, NettyConfig.WRITE_TIME_OUT, NettyConfig.CLIENT_READ_WRITE_TIME_OUT));
                        ch.pipeline().addLast(new ClientHeartBeatHandler());
                        ch.pipeline().addLast(new ClientOnlineMonitorHandler(self));
                        ch.pipeline().addLast(new ClientMessageHandler());
                    }
                });
    }

    public void connect() {
        this.reconnect();
    }

    public void reconnect() {
        this.future = this.bootstrap.connect(NettyConfig.HOST, NettyConfig.PORT);
        this.future.addListener((ChannelFutureListener) future -> {
            if(future.cause() != null) {
                log.error("重连失败，等待下一次重连！", future.cause());
                future.channel().eventLoop().schedule(this::reconnect, NettyConfig.RECONNECT, TimeUnit.SECONDS);
                return;
            }
            Channel channel = future.channel();
            if (channel != null && channel.isActive()) {
                log.info("重连成功：{}！", channel.remoteAddress());
            }
        });
    }

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient();
        client.init();
        client.connect();
        try {
            String msg = null;
            Scanner scanner = new Scanner(System.in);
            while (!(msg = scanner.nextLine()).equals("exit")) {
                client.getFuture().channel().writeAndFlush(msg + DELIMITER);
            }
        } finally {
            client.getClient().shutdownGracefully();
            client.getFuture().channel().closeFuture().sync();
        }
    }
}
