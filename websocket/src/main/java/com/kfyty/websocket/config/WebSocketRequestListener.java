package com.kfyty.websocket.config;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 描述: websocket 监听器，用于设置 websocket 客户端 IP
 *
 * @author fyty
 * @date 2021/5/15 21:53
 * @email kfyty725@hotmail.com
 */
@Component
@WebListener
public class WebSocketRequestListener implements ServletRequestListener {
    public static final String WEB_SOCKET_CLIENT_IP = "WEB_SOCKET_CLIENT_IP";

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
        String upgrade = request.getHeader("upgrade");
        if("websocket".equalsIgnoreCase(upgrade)) {
            HttpSession session = request.getSession();
            session.setAttribute(WEB_SOCKET_CLIENT_IP, request.getRemoteHost() + ":" + request.getRemotePort());
        }
    }
}
