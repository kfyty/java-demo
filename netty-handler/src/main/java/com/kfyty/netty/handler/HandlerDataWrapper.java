package com.kfyty.netty.handler;

/**
 * 描述: 数据包装器
 *
 * @author kfyty725
 * @date 2021/4/30 15:42
 * @email kfyty725@hotmail.com
 */
public interface HandlerDataWrapper {
    Object wrap(Object source);
}
