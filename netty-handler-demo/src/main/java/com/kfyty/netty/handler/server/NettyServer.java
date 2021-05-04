package com.kfyty.netty.handler.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kfyty.netty.handler.codec.BaseHandlerModelDecoder;
import com.kfyty.netty.handler.config.NettyConfig;
import com.kfyty.netty.handler.handler.NettyDispatcherHandler;
import com.kfyty.netty.handler.handler.OnlineMonitorHandler;
import com.kfyty.netty.handler.handler.StringMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

import static com.kfyty.netty.handler.config.NettyConfig.DELIMITER;
import static com.kfyty.netty.handler.config.NettyConfig.PORT;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/8 11:09
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Configuration
public class NettyServer implements CommandLineRunner, DisposableBean {
    private final NioEventLoopGroup serverGroup;
    private final NioEventLoopGroup clientGroup;
    private final ServerBootstrap serverBootstrap;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NettyDispatcherHandler nettyDispatcherHandler;

    public NettyServer() {
        this.serverGroup = new NioEventLoopGroup();
        this.clientGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    @Override
    public void run(String... args) throws Exception {
        serverBootstrap.group(serverGroup, clientGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_SNDBUF, NettyConfig.DEFAULT_BUF)
                .childOption(ChannelOption.SO_RCVBUF, NettyConfig.DEFAULT_BUF)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(NettyConfig.DEFAULT_BUF, Unpooled.copiedBuffer(DELIMITER.getBytes(StandardCharsets.UTF_8))));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new OnlineMonitorHandler());
                        ch.pipeline().addLast(new BaseHandlerModelDecoder(objectMapper));
                        ch.pipeline().addLast(nettyDispatcherHandler);
                        ch.pipeline().addLast(new StringMessageHandler());
                        log.info("IP:{} connected !", ch.remoteAddress());
                    }
                })
                .bind(PORT)
                .sync();
    }

    @Override
    public void destroy() throws Exception {
        clientGroup.shutdownGracefully();
        serverGroup.shutdownGracefully();
    }
}
