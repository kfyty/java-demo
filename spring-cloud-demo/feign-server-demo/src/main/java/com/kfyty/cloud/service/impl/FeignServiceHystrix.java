package com.kfyty.cloud.service.impl;

import com.kfyty.cloud.service.FeignService;
import org.springframework.stereotype.Component;

/**
 * 功能描述:
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 14:28
 * @since JDK 1.8
 */
@Component
public class FeignServiceHystrix implements FeignService {
    @Override
    public String feignService(String name) {
        return "server error: " + name;
    }
}
