package com.kfyty.cloud.hystrix;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/12/8 11:50
 * @email kfyty725@hotmail.com
 */
@Component
public class ProducerFallback implements FallbackProvider {
    /**
     * 服务名称，可以是 *
     * @return 服务名
     */
    @Override
    public String getRoute() {
        return "eureka-service-producer";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(cause.getMessage().getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return null;
            }
        };
    }
}
