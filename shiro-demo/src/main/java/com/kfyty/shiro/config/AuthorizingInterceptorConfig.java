package com.kfyty.shiro.config;

import com.kfyty.mybatis.auto.mapper.utils.CommonUtil;
import com.kfyty.shiro.utils.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Configuration
public class AuthorizingInterceptorConfig extends WebMvcConfigurationSupport implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestContext.set(request);
        if(handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if(method.isAnnotationPresent(RequiresPermissions.class)) {
                String[] permission = request.getParameterValues("permission");
                if(CommonUtil.empty(permission)) {
                    throw new IllegalArgumentException("缺少许可证参数！");
                }
                for (int i = 0; i < permission.length; i++) {
                    permission[i] = permission[i] + ":" + request.getRequestURI();
                }
                RequiresPermissions annotation = method.getAnnotation(RequiresPermissions.class);
                CommonUtil.setAnnotationValue(annotation, "value", permission);
            }
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        RequestContext.clear();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*.html").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/static/lib/");
    }
}
