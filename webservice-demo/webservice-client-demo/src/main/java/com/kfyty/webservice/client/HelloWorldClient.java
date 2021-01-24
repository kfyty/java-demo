package com.kfyty.webservice.client;

import com.kfyty.webservice.service.HelloWorld;
import com.kfyty.webservice.utils.WebServiceUtil;

/**
 * 功能描述:
 *
 * @author zhangkun@wisdombud.com
 * @date 2020/1/10 11:26
 * @since JDK 1.8
 */
public class HelloWorldClient {

    public static void main(String[] args) throws Exception {
        HelloWorld helloWorld = WebServiceUtil.getWebServiceObject(HelloWorld.class, HelloWorld.WSDL);
        System.out.println(helloWorld.hello("kfyty") + "@" + helloWorld.valid());
    }
}
