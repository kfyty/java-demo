package com.kfyty.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * 功能描述: 过滤器
 *
 * @author zhangkun@wisdombud.com
 * @date 2019/9/30 14:49
 * @since JDK 1.8
 */
@Component
public class CustomZuulFilter extends ZuulFilter {
    /**
     * filterType：返回一个字符串代表过滤器的类型，在 zuul 中定义了四种不同生命周期的过滤器类型，具体如下：
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否要过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体过滤逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("custom zuul filter !");
        return null;
    }
}
