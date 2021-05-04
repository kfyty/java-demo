package com.kfyty.netty.handler.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ReflectUtil {

    public static Class<?> getGeneric(Type type, int genericIndex) {
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getActualTypeArguments()[genericIndex];
        }
        return null;
    }
}
