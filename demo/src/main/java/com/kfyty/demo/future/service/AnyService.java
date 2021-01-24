package com.kfyty.demo.future.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnyService {
    public String anyMethod(String s) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "any method: " + s;
    }

    public String anyExceptionMethod(String s) {
        throw new RuntimeException("any exception method: " + s);
    }

    public String anyMethod(List<String> ss) {
        return "any method: " + ss;
    }
}
