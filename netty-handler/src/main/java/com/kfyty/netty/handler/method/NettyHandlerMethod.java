package com.kfyty.netty.handler.method;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/4/30 15:20
 * @email kfyty725@hotmail.com
 */
@Getter
@AllArgsConstructor
public class NettyHandlerMethod {
    private final Object controller;
    private final Method controllerMethod;

    public Object invoke(Object ... args) throws ReflectiveOperationException {
        return this.controllerMethod.invoke(this.controller, args);
    }
}
