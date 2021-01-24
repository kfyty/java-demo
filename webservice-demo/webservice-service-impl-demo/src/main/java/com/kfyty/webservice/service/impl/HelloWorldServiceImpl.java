package com.kfyty.webservice.service.impl;

import com.kfyty.webservice.service.HelloWorld;
import com.kfyty.webservice.utils.WebServiceUtil;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class HelloWorldServiceImpl implements HelloWorld {

    @Override
    public String hello(String content) {
        return "hello: " + content;
    }

    @Override
    public String valid() {
        return String.valueOf(super.hashCode());
    }

    public static void main(String[] args) throws Exception {
        Endpoint.publish(ADDRESS, WebServiceUtil.initWebServiceConfig(new HelloWorldServiceImpl()));
        System.out.println("publish success !");
    }
}
