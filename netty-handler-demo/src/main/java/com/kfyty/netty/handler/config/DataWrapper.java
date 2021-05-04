package com.kfyty.netty.handler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kfyty.netty.handler.HandlerDataWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataWrapper implements HandlerDataWrapper {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Object wrap(Object source) {
        return objectMapper.writeValueAsString(source) + NettyConfig.DELIMITER;
    }
}
