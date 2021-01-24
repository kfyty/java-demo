package com.kfyty.webservice.service;

import javax.jws.WebService;

/**
 * 功能描述:
 *
 * @author zhangkun@wisdombud.com
 * @date 2020/1/10 11:16:37
 * @since JDK 1.8
 */
@WebService(serviceName = "helloWorld", targetNamespace = "http://service/helloWorld")
public interface HelloWorld {
    String ADDRESS = "http://localhost:7070/webservice";

    String WSDL = ADDRESS + "?wsdl";

    String hello(String content);

    String valid();
}
