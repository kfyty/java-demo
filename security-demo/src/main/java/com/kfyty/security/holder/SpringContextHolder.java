package com.kfyty.security.holder;

import com.kfyty.security.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 16:20
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Component
public class SpringContextHolder implements ApplicationContextAware, BeanPostProcessor {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static HttpServletRequest currentRequest() {
        try {
            return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        } catch (IllegalStateException e) {
            throw new BusinessException("the current thread is not bound to HttpServletRequest !", e);
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
