package com.kfyty.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/03/19 20:29
 * @email kfyty725@hotmail.com
 */
@Configuration
public class LettuceConfig {

    @Bean
    public GenericObjectPoolConfig<Object> lettucePoolConfig(RedisProperties redisProperties) {
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        poolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getLettuce().getPool().getTimeBetweenEvictionRuns().toMillis());
        return poolConfig;
    }

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig<Object> poolConfig, RedisProperties redisProperties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        config.setDatabase(redisProperties.getDatabase());
        return new LettuceConnectionFactory(config, LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build());
    }
}
