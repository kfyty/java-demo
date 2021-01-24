package com.kfyty.shiro.utils;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestContext {
    private static final ThreadLocal<HttpServletRequest> REQUEST_LOCAL = new ThreadLocal<>();

    public static void set(HttpServletRequest request) {
        Assert.notNull(request, "The request is null !");
        REQUEST_LOCAL.set(request);
    }

    public static HttpServletRequest get() {
        HttpServletRequest request = REQUEST_LOCAL.get();
        if(request == null) {
            throw new RuntimeException("The request context could not be found in this thread !");
        }
        return request;
    }

    public static void clear() {
        REQUEST_LOCAL.remove();
    }
}
