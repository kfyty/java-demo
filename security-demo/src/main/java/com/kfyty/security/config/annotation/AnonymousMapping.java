package com.kfyty.security.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/20 17:12
 * @email kfyty725@hotmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AnonymousMapping {
}
