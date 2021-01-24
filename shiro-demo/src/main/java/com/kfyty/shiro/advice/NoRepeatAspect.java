package com.kfyty.shiro.advice;

import com.kfyty.shiro.annotation.NoRepeat;
import com.kfyty.shiro.entity.User;
import com.kfyty.shiro.utils.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Aspect
@Component
public class NoRepeatAspect {
    private static final String TOKEN_NAME = "_NO_REPEAT_TOKEN_NAME_";
    private static final Lock NO_REPEAT_LOCK = new ReentrantLock();

    @Pointcut("@annotation(noRepeat)")
    public void noRepeatPointCut(NoRepeat noRepeat) {

    }

    @Around(value = "noRepeatPointCut(noRepeat)", argNames = "pjp, noRepeat")
    public Object noRepeatHandle(ProceedingJoinPoint pjp, NoRepeat noRepeat) throws Throwable {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Object token = RequestContext.get().getSession().getAttribute(TOKEN_NAME);
        if(token != null && token.equals(user.getId())) {
            throw new IllegalArgumentException("重复提交！");
        }
        try {
            NO_REPEAT_LOCK.lock();
            token = RequestContext.get().getSession().getAttribute(TOKEN_NAME);
            if(token != null && token.equals(user.getId())) {
                throw new IllegalArgumentException("重复提交！");
            }
            RequestContext.get().getSession().setAttribute(TOKEN_NAME, user.getId());
            return pjp.proceed();
        } finally {
            RequestContext.get().getSession().removeAttribute(TOKEN_NAME);
            NO_REPEAT_LOCK.unlock();
        }
    }
}
