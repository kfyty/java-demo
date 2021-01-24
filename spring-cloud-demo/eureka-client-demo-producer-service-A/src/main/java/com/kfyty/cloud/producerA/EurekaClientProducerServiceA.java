package com.kfyty.cloud.producerA;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 11:30
 * @since JDK 1.8
 */
@RestController
@EnableEurekaClient
@SpringBootApplication
public class EurekaClientProducerServiceA {
    @Value("${server.port}")
    private String port;

    @RequestMapping("service/producer/{name}")
    public String producerService(@PathVariable("name") String name) {
        return "hello producer service: " + name + ", from port: " + port;
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientProducerServiceA.class, args);
    }
}
