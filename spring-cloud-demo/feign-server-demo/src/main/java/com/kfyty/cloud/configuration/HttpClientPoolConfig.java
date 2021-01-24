package com.kfyty.cloud.configuration;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述:
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/10/14 11:47
 * @since JDK 1.8
 */
@Configuration
public class HttpClientPoolConfig {

    @Bean
    public HttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(this.createRequestConfig())
                .setConnectionManager(this.createPoolingHttpClientConnectionManager())
                .build();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()               // 生成默认请求配置
                .setSocketTimeout(5 * 1000)         // 超时时间
                .setConnectTimeout(3 * 1000)        // 连接时间
                .build();
    }

    private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager() {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.MILLISECONDS);

        // 总连接数
        pollingConnectionManager.setMaxTotal(5000);

        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(100);

        return pollingConnectionManager;
    }
}
