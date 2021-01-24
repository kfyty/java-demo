package com.kfyty.cloud.service;

import com.kfyty.cloud.service.impl.FeignServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能描述: fallback: 熔断回调方法，即其实现方法
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 14:08
 * @since JDK 1.8
 */
@FeignClient(value = "eureka-service-producer", fallback = FeignServiceHystrix.class)
public interface FeignService {
    @RequestMapping("service/producer/{name}")
    String feignService(@PathVariable("name") String name);
}
