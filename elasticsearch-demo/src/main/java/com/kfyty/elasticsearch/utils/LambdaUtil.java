package com.kfyty.elasticsearch.utils;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/3 9:33
 * @email kfyty725@hotmail.com
 */
public class LambdaUtil {
    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    public static <T> String convertLambdaMethodName(SerializableFunction<T, ?> func) {
        String implMethodName = resolve(func).getImplMethodName();
        if(implMethodName.length() < 4) {
            return implMethodName.replace("set", "").toLowerCase();
        }
        return Character.toLowerCase(implMethodName.charAt(3)) + implMethodName.substring(4);
    }

    public static <T> SerializedLambda resolve(SerializableFunction<T, ?> func) {
        Class<?> clazz = func.getClass();
        return Optional.ofNullable(FUNC_CACHE.get(clazz))
                .map(WeakReference::get)
                .orElseGet(() -> {
                    try {
                        Method method = clazz.getDeclaredMethod("writeReplace");
                        method.setAccessible(Boolean.TRUE);
                        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(func);
                        FUNC_CACHE.put(clazz, new WeakReference<>(serializedLambda));
                        return serializedLambda;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
