package com.kfyty.elasticsearch.utils;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/3 9:39
 * @email kfyty725@hotmail.com
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Serializable {
    R apply(T t);
}
