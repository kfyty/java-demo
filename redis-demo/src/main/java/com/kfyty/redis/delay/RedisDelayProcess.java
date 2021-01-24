package com.kfyty.redis.delay;

import com.kfyty.redis.delay.handler.RedisDelayHandler;
import com.kfyty.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
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
    private static final int DEFAULT_DAEMON_PERIOD = 5;

    private static final String DEFAULT_VALUE = "REDIS_DELAY_PROCESS:DEFAULT_VALUE";

    private volatile boolean start;

    private volatile boolean finish;

    private final ExecutorService executor;

    private final Map<String, RedisDelayHandler> handler;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisDelayProcess(Map<String, RedisDelayHandler> handler) {
        this.handler = handler;
        this.executor = new ThreadPoolExecutor(1, 2, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
    }

    @Override
    public void destroy() throws Exception {
        this.finish = true;
        log.info("waiting load redis delay task finish ...");
        executor.shutdown();
    }

    @Override
    public void run(String ... args) {
        executor.submit(this);
    }

    @Override
    public void run() {
        try {
            log.info("load redis delay task start !");
            while(!RedisDelayProcess.this.finish) {
                this.start = true;
                for (Map.Entry<String, RedisDelayHandler> handler : handler.entrySet()) {
                    long maxScore = System.currentTimeMillis() - handler.getValue().timeout();
                    Set<String> dataSet = redisTemplate.opsForZSet().rangeByScore(handler.getKey(), 0, maxScore);
                    if(CollectionUtils.isEmpty(dataSet)) {
                        continue;
                    }
                    for (String data : dataSet) {
                        if(!RedisUtil.lock(data, DEFAULT_VALUE, handler.getValue().lockTimeout())) {
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
            }
        } catch (Throwable throwable) {
            this.start = false;
            executor.submit(new RedisDelayProcessDaemon());
            log.error("load redis delay task error and waiting daemon !", throwable);
        }
        log.info("load redis delay task finished !");
    }

    private class RedisDelayProcessDaemon implements Runnable {

        @Override
        public void run() {
            try {
                log.info("redis delay task daemon start !");
                while(!RedisDelayProcess.this.finish) {
                    if(RedisDelayProcess.this.start) {
                        break;
                    }
                    try {
                        redisTemplate.execute((RedisCallback<Object>) connection -> connection.execute("INFO"));
                        executor.submit(RedisDelayProcess.this);
                        log.info("try connect redis success !");
                        break;
                    } catch (Exception e) {
                        log.error("try connect redis failed, will try in {} seconds, error info: {} ！", DEFAULT_DAEMON_PERIOD, e.getMessage());
                        TimeUnit.SECONDS.sleep(DEFAULT_DAEMON_PERIOD);
                    }
                }
            } catch (Throwable throwable) {
                log.error("redis delay task daemon error !", throwable);
            }
            log.info("redis delay task daemon finished !");
        }
    }
}
