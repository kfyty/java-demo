package com.kfyty.websocket.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * 描述: websocket 端点配置，把客户端 IP 设置到用户属性中
 *
 * @author fyty
 * @date 2021/5/15 21:54
 * @email kfyty725@hotmail.com
 */
@Component
public class WebSocketConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketConfigurator.applicationContext = applicationContext;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(config, request, response);
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put(WebSocketRequestListener.WEB_SOCKET_CLIENT_IP, httpSession.getAttribute(WebSocketRequestListener.WEB_SOCKET_CLIENT_IP));
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        if(clazz.isAnnotationPresent(Configurable.class)) {
            return super.getEndpointInstance(clazz);
        }
        return WebSocketConfigurator.applicationContext.getBean(clazz);
    }
}
