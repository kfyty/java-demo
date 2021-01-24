package com.kfyty.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 功能描述: 路由，根据配置转发到 ribbon/feign
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 14:39
 * @since JDK 1.8
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class ZuulServer {
    public static void main(String[] args) {
        SpringApplication.run(ZuulServer.class, args);
    }
}
