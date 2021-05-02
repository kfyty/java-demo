package com.kfyty.jsp.template;

import com.kfyty.demo.template.HelloWorld;

public class HelloWorldImpl implements HelloWorld {
    @Override
    public void hello(String name) {
        System.out.println("hello: " + name);
    }
}
