package com.kfyty.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 功能描述: 实现了负载均衡，添加 @EnableHystrix 注解，支持了熔断能力
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 12:01
 * @since JDK 1.8
 */
@RestController
@EnableHystrix
@EnableEurekaClient
@SpringBootApplication
public class RibbonServer {
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 支持熔断
     * fallbackMethod: 回调方法，其中接口方法参数会应用到回调方法中
     */
    @HystrixCommand(fallbackMethod = "onError")
    @RequestMapping("service/producer/{name}")
    public String ribbonService(@PathVariable("name") String name) {
        return restTemplate.getForObject("http://eureka-service-producer/service/producer/{name}", String.class, name);
    }

    public String onError(String name) {
        return "server error: " + name;
    }

    public static void main(String[] args) {
        SpringApplication.run(RibbonServer.class, args);
    }
}
