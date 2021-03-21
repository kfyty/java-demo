package com.kfyty.redis.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 描述: 防重复提交
 *
 * @author kfyty
 * @date 2020/11/12 14:32
 * @email kfyty725@hotmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdempotentCommit {
    /**
     * 方法参数索引，小于 0 时表示不使用方法参数
     * @return 默认值 0
     */
    int argIndex() default 0;

    /**
     * 锁过期时间
     * @return 默认值 5 分钟
     */
    long expire() default 5 * 60;

    /**
     * 尝试获取锁等待时间
     * @return 默认值 1 分钟
     */
    long tryWait() default 1;

    /**
     * 时间单位
     * @return 默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
