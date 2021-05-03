package com.kfyty.redis.delay.zset;

import com.kfyty.redis.delay.zset.handler.RedisDelayHandler;
import com.kfyty.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/13 10:42
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Component
public class RedisDelayProcess implements CommandLineRunner, Runnable, DisposableBean {
    private static final int DEFAULT_RETRY_PERIOD = 5;

    private volatile boolean finish = true;

    private final Map<String, RedisDelayHandler> handler;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisDelayProcess(Map<String, RedisDelayHandler> handler) {
        this.handler = handler;
    }

    @Override
    public void destroy() throws Exception {
        if(!this.finish) {
            this.finish = true;
            log.info("waiting load redis delay task finish ...");
        }
    }

    @Override
    public void run(String ... args) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        this.finish = false;
        log.info("load redis delay task start !");
        while(!RedisDelayProcess.this.finish) {
            try {
                for (Map.Entry<String, RedisDelayHandler> handler : handler.entrySet()) {
                    long maxScore = System.currentTimeMillis() - handler.getValue().timeout();
                    Set<String> dataSet = redisTemplate.opsForZSet().rangeByScore(handler.getKey(), 0, maxScore);
                    if(CollectionUtils.isEmpty(dataSet)) {
                        continue;
                    }
                    for (String data : dataSet) {
                        if(!RedisUtil.tryLock(data, handler.getValue().lockTimeout(), handler.getValue().tryWaitTimeout(), handler.getValue().timeoutUnit())) {
                            continue;
                        }
                        try {
                            if(redisTemplate.opsForZSet().rank(handler.getKey(), data) == null) {
                                continue;
                            }
                            if(handler.getValue().doHandle(data)) {
                                redisTemplate.opsForZSet().remove(handler.getKey(), data);
                            }
                        } finally {
                            RedisUtil.unlock(data);
                        }
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    log.error("load redis delay task error on sleep, and continue load redis delay task !", e);
                }
            } catch (Throwable throwable) {
                log.error("load redis delay task error and retry in {} seconds !", DEFAULT_RETRY_PERIOD, throwable);
                try {
                    TimeUnit.SECONDS.sleep(DEFAULT_RETRY_PERIOD);
                } catch (InterruptedException e) {
                    log.error("redis delay task retry interrupted !", e);
                }
            }
        }
        log.info("load redis delay task finished !");
    }
}
