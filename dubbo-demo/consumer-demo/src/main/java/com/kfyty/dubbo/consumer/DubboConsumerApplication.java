package com.kfyty.dubbo.consumer;

import com.kfyty.dubbo.service.DubboService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DubboConsumerApplication {
    private static DubboService dubboService;

    @Reference
    public void setDubboService(DubboService dubboService) {
        DubboConsumerApplication.dubboService = dubboService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class);
        System.out.println(dubboService.anyService("hello"));
    }
}
