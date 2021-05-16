package com.kfyty.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

/**
 * 描述: EnableLoadTimeWeaving + EnableSpringConfigured + javaagent:spring-instrument.jar
 * 可以实现为 Configurable 注解的类自动注入，但是该类不能有 Component 注解
 *
 * 当类上存在 Component + Configurable 时，可以使用 aspectj-maven-plugin 插件实现类似的功能，
 * 且不再需要 EnableSpringConfigured + javaagent:spring-instrument.jar
 *
 * @author fyty
 * @date 2021/5/16 10:40
 * @email kfyty725@hotmail.com
 */
@EnableLoadTimeWeaving
@SpringBootApplication
@EnableSpringConfigured
public class WebSocketApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebSocketApplication.class, args);
    }
}
