package com.kfyty.netty.handler.config;

import com.kfyty.netty.handler.annotation.NettyController;
import com.kfyty.netty.handler.annotation.NettyMapping;
import com.kfyty.netty.handler.codec.BaseHandlerModelDecoder;
import com.kfyty.netty.handler.handler.NettyDispatcherHandler;
import com.kfyty.netty.handler.method.NettyHandlerMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 描述: 扫描 netty 控制器配置
 *
 * @author kfyty725
 * @date 2021/4/30 14:27
 * @email kfyty725@hotmail.com
 */
@Configuration
@Import({NettyDispatcherHandler.class, BaseHandlerModelDecoder.class})
@ComponentScan(includeFilters = @ComponentScan.Filter(NettyController.class))
public class NettyHandlerConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private NettyDispatcherHandler nettyDispatcherHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            this.doConfigHandler(event);
        }
    }

    private void doConfigHandler(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(NettyController.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object controller = entry.getValue();
            NettyMapping mapping = controller.getClass().getAnnotation(NettyMapping.class);
            this.doConfigHandler(controller, mapping == null || StringUtils.isEmpty(mapping.value()) ? "" : mapping.value());
        }
    }

    private void doConfigHandler(Object controller, String parentPath) {
        ReflectionUtils.doWithMethods(controller.getClass(), method -> {
            NettyMapping mapping = method.getAnnotation(NettyMapping.class);
            String path = formatPath(parentPath) + formatPath(mapping.value());
            NettyHandlerMethod controllerMethod = new NettyHandlerMethod(controller, method);
            this.nettyDispatcherHandler.getControllerMap().put(path, controllerMethod);
        }, method -> method.isAnnotationPresent(NettyMapping.class));
    }

    private String formatPath(String path) {
        if("".equals(path)) {
            return path;
        }
        if(!path.startsWith("/")) {
            path = '/' + path;
        }
        if(path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
