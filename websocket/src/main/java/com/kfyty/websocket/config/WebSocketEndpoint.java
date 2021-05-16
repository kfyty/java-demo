package com.kfyty.websocket.config;

import com.kfyty.websocket.WebSocketApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 描述: websocket 端点
 *
 * @author fyty
 * @date 2021/5/15 22:35
 * @email kfyty725@hotmail.com
 */
@Component
@Configurable
@ServerEndpoint(value = "/websocket/endpoint", configurator = WebSocketConfigurator.class)
public class WebSocketEndpoint {
    private static final Logger log = LoggerFactory.getLogger(WebSocketEndpoint.class);

    @Autowired
    private WebSocketApplication webSocketApplication;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        log.info("客户端已连接：{}", this.getRemoteAddress(session));
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if("ping".equals(message)) {
            session.getAsyncRemote().sendText("pong");
            return;
        }
        log.info("收到客户端 {} 消息：{}", this.getRemoteAddress(session), message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        log.info("客户端已断开连接：{}", this.getRemoteAddress(session));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("客户端发生异常：{}", this.getRemoteAddress(session), error);
    }

    public String getRemoteAddress(Session session) {
        return (String) session.getUserProperties().get(WebSocketRequestListener.WEB_SOCKET_CLIENT_IP);
    }
}
