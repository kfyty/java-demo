package com.kfyty.netty.handler.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kfyty.netty.handler.HandlerDataWrapper;
import com.kfyty.netty.handler.annotation.NettyParam;
import com.kfyty.netty.handler.method.NettyHandlerMethod;
import com.kfyty.netty.handler.model.BaseHandlerModel;
import com.kfyty.netty.handler.utils.ReflectUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述: netty 控制器处理器
 *
 * @author kfyty725
 * @date 2021/4/30 14:43
 * @email kfyty725@hotmail.com
 */
@Configuration
@ChannelHandler.Sharable
public class NettyDispatcherHandler extends SimpleChannelInboundHandler<BaseHandlerModel> {
    @Getter
    private final Map<String, NettyHandlerMethod> controllerMap = new ConcurrentHashMap<>();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HandlerDataWrapper handlerDataWrapper;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseHandlerModel msg) throws Exception {
        if(StringUtils.isEmpty(msg.getNettyPath())) {
            String error = "netty handler require nettyPath parameter, msg=[" + msg + "]";
            ctx.channel().writeAndFlush(this.handlerDataWrapper.wrap(error));
            throw new IllegalArgumentException(error);
        }
        NettyHandlerMethod controllerMethod = this.controllerMap.get(msg.getNettyPath());
        if(controllerMethod == null) {
            String error = "handler method not found for path: [" + msg.getNettyPath() + "]";
            ctx.channel().writeAndFlush(this.handlerDataWrapper.wrap(error));
            throw new IllegalArgumentException(error);
        }
        Object result = controllerMethod.invoke(this.processMethodArgs(ctx, controllerMethod.getControllerMethod(), msg));
        ctx.channel().writeAndFlush(this.handlerDataWrapper.wrap(result));
    }

    private Object[] processMethodArgs(ChannelHandlerContext ctx, Method method, BaseHandlerModel msg) throws IOException {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = processMethodArgs(ctx, parameters[i], msg);
        }
        return args;
    }

    private Object processMethodArgs(ChannelHandlerContext ctx, Parameter parameter, BaseHandlerModel msg) throws IOException {
        Class<?> parameterType = parameter.getType();
        if(ChannelHandlerContext.class.isAssignableFrom(parameterType)) {
            return ctx;
        }
        if(BaseHandlerModel.class.isAssignableFrom(parameterType)) {
            return this.objectMapper.readValue(msg.getOriginJson(), parameterType);
        }
        NettyParam nettyParam = parameter.getAnnotation(NettyParam.class);
        if(nettyParam == null) {
            throw new IllegalArgumentException("NettyParam annotation is missing !");
        }
        ObjectNode jsonNodes = this.objectMapper.readValue(msg.getOriginJson(), ObjectNode.class);
        JsonNode jsonNode = jsonNodes.get(nettyParam.value());
        return processMethodArgs(parameterType, parameter.getParameterizedType(), jsonNode);
    }

    private Object processMethodArgs(Class<?> parameterType, Type type, JsonNode jsonNode) {
        if(parameterType.equals(Boolean.class) || parameterType.equals(boolean.class)) {
            return jsonNode.asBoolean();
        }
        if(parameterType.equals(Byte.class) || parameterType.equals(byte.class)) {
            return Integer.valueOf(jsonNode.asInt()).byteValue();
        }
        if(parameterType.equals(Short.class) || parameterType.equals(short.class)) {
            return Integer.valueOf(jsonNode.asInt()).shortValue();
        }
        if(parameterType.equals(Integer.class) || parameterType.equals(int.class)) {
            return jsonNode.asInt();
        }
        if(parameterType.equals(Long.class) || parameterType.equals(long.class)) {
            return jsonNode.asLong();
        }
        if(parameterType.equals(Float.class) || parameterType.equals(float.class)) {
            return Double.valueOf(jsonNode.asDouble()).floatValue();
        }
        if(parameterType.equals(Double.class) || parameterType.equals(double.class)) {
            return jsonNode.asDouble();
        }
        if(parameterType.equals(BigInteger.class)) {
            return jsonNode.bigIntegerValue();
        }
        if(parameterType.equals(BigDecimal.class)) {
            return jsonNode.decimalValue();
        }
        if(parameterType.equals(String.class)) {
            return jsonNode.asText();
        }
        if(type != null && Collection.class.isAssignableFrom(parameterType)) {
            List<Object> values = new ArrayList<>();
            Class<?> generic = ReflectUtil.getGeneric(type, 0);
            for(Iterator<JsonNode> i = jsonNode.elements(); i.hasNext(); ) {
                values.add(processMethodArgs(generic, null, i.next()));
            }
            if(parameterType.equals(List.class)) {
                return values;
            }
            if(parameterType.equals(Set.class)) {
                return new HashSet<>(values);
            }
        }
        return null;
    }
}
