package com.kfyty.webservice.utils;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class WebServiceUtil {
    public static <T> T initWebServiceConfig(T webserviceImpl) throws Exception {
        Class<?> interfaces = webserviceImpl.getClass().getInterfaces()[0];
        WebService interfaceAnnotation = interfaces.getAnnotation(WebService.class);
        WebService implAnnotation = webserviceImpl.getClass().getAnnotation(WebService.class);
        CommonUtil.setAnnotationValue(implAnnotation, "serviceName", interfaceAnnotation.serviceName());
        CommonUtil.setAnnotationValue(implAnnotation, "targetNamespace", interfaceAnnotation.targetNamespace());
        CommonUtil.setAnnotationValue(implAnnotation, "endpointInterface", interfaces.getName());
        return webserviceImpl;
    }

    public static <T> T getWebServiceObject(Class<T> clazz, String wsdl) throws MalformedURLException {
        WebService webService = clazz.getAnnotation(WebService.class);
        QName serviceName = new QName(webService.targetNamespace(), webService.serviceName());
        Service service = Service.create(new URL(wsdl), serviceName);
        return service.getPort(clazz);
    }
}
