package com.kfyty.jsp.template;

import com.kfyty.demo.template.HelloWorld;

/**
 * 动态生成及编译并执行测试
 */
public class HelloWorldImpl implements HelloWorld {
    @Override
    public void hello(String name) {
        System.out.println("hello: " + name);
    }
}
