package com.kfyty.netty.handler.controller;

import com.kfyty.netty.handler.annotation.NettyController;
import com.kfyty.netty.handler.annotation.NettyMapping;
import com.kfyty.netty.handler.annotation.NettyParam;
import com.kfyty.netty.handler.model.UserModel;

import java.util.List;

@NettyController
@NettyMapping("netty")
public class HelloController {

    @NettyMapping("test")
    public String test(@NettyParam("name") String name) {
        return "test: " + name;
    }

    @NettyMapping("hello")
    public String hello(@NettyParam("names") List<String> names) {
        return "hello: " + names;
    }

    @NettyMapping("user")
    public String user(UserModel user, @NettyParam("userId") String userId) {
        return "hello: " + userId + ":" + user.getUserName();
    }
}
