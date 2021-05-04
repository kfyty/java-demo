package com.kfyty.netty.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/4/30 14:25
 * @email kfyty725@hotmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NettyMapping {
    String value() default "";
}
