package com.kfyty.redis.config.aspect;

import com.kfyty.redis.config.annotation.IdempotentCommit;
import com.kfyty.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 描述: 防重复提交切面
 *
 * @author kfyty
 * @date 2020/11/12 14:33
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Aspect
@Component
public class IdempotentCommitAspect {
    private static final String KEY_PREFIX = "IDEMPOTENT_COMMIT:";

    private static final String DEFAULT_VALUE = KEY_PREFIX + "VALUE";

    @Around(value = "@annotation(idempotentCommit)")
    public Object around(ProceedingJoinPoint pjp, IdempotentCommit idempotentCommit) throws Throwable {
        Object[] args = pjp.getArgs();
        int argIndex = idempotentCommit.argIndex();
        if(argIndex >= args.length) {
            log.error("{}: argIndex out of bound: argIndex={}, args.length={} !", pjp.getSignature().getName(), argIndex, args.length);
            return null;
        }
        if(argIndex > -1 && args[argIndex] == null) {
            log.error("{} args: args[{}] is null !", pjp.getSignature().getName(), argIndex);
            return null;
        }
        String key = this.buildKey(pjp.getSignature(), argIndex, args);
        if(!RedisUtil.lock(key, DEFAULT_VALUE, TimeUnit.MILLISECONDS.convert(idempotentCommit.expire(), idempotentCommit.timeUnit()))) {
            log.error("repeated submit !");
            return "repeated submit !";
        }
        try {
            return pjp.proceed();
        } finally {
            RedisUtil.unlock(key);
        }
    }

    private String buildKey(Signature signature, int argIndex, Object[] args) {
        StringBuilder key = new StringBuilder(KEY_PREFIX);
        key.append(signature.getDeclaringTypeName()).append(":").append(signature.getName());
        if(argIndex > -1) {
            key.append(":").append(args[argIndex]);
        }
        return key.toString();
    }
}
