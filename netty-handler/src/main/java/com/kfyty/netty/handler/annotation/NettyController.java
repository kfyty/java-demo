package com.kfyty.netty.handler.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/4/30 14:22
 * @email kfyty725@hotmail.com
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyController {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
