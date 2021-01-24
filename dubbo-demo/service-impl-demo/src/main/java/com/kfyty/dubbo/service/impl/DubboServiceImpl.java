package com.kfyty.dubbo.service.impl;

import com.kfyty.dubbo.service.DubboService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DubboServiceImpl implements DubboService {
    @Override
    public String anyService(String any) {
        if(any == null) {
            throw new NullPointerException("any can't null !");
        }
        return "dubbo: " + any;
    }
}
