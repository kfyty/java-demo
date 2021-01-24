package com.kfyty.shiro.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Around("execution(* com.kfyty.shiro.controller.*.*(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        log.info("around start ...");
        Object returnValue = point.proceed();
        log.info("around end and result is {} !", returnValue);
        return returnValue;
    }
}
