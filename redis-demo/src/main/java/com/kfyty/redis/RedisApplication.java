package com.kfyty.redis;

import com.kfyty.redis.config.annotation.IdempotentCommit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 11:14
 * @email kfyty725@hotmail.com
 */
@RestController
@SpringBootApplication
public class RedisApplication {

    @IdempotentCommit
    @GetMapping("test")
    public String repeat(String token) throws Exception {
        TimeUnit.SECONDS.sleep(5);
        return token;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RedisApplication.class, args);
    }
}
