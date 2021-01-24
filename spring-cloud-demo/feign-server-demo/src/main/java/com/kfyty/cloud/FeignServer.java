package com.kfyty.cloud;

import com.kfyty.cloud.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述: feign: 整合了 ribbon/hystrix
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 14:06
 * @since JDK 1.8
 */
@RestController
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class FeignServer {
    @Autowired
    private FeignService feignService;

    @RequestMapping("service/producer/{name}")
    public String feignService(@PathVariable("name") String name) {
        return feignService.feignService(name);
    }

    public static void main(String[] args) {
        SpringApplication.run(FeignServer.class, args);
    }
}
