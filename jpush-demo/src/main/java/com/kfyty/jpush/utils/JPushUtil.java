package com.kfyty.jpush.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NettyHttpClient;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.kfyty.jpush.config.JPushConfig;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/12/14 11:54
 * @email kfyty725@hotmail.com
 */
@Slf4j
public abstract class JPushUtil {
    public static void pushMessage(String content) {
        pushMessage(content, new HashMap<>());
    }

    public static void pushMessage(String content, Map<String, String> extrasMap) {
        pushMessage(null, content, extrasMap);
    }

    public static void pushMessage(String title, String content, Map<String, String> extrasMap) {
        pushMessage(newPushPayload(title, content, extrasMap));
    }

    public static void pushMessage(String title, String content, Map<String, String> extrasMap, String ... aliases) {
        pushMessage(title, content, extrasMap, new ArrayList<>(Arrays.asList(aliases)));
    }

    public static void pushMessage(String title, String content, Map<String, String> extrasMap, List<String> aliases) {
        pushMessage(newPushPayload(title, content, extrasMap, aliases));
    }

    public static void pushMessage(PushPayload pushPayload) {
        pushMessage(pushPayload, response -> {
            if (response.responseCode == HttpStatus.OK.value()) {
                log.info("jPush push message success:[{}] !", response);
            } else {
                log.error("jPush push message failed:[{}] !", response);
            }
        });
    }

    public static void pushMessage(PushPayload pushPayload, NettyHttpClient.BaseCallback callback) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        String host = (String) clientConfig.get(ClientConfig.PUSH_HOST_NAME);
        String authorization = ServiceHelper.getBasicAuthorization(JPushConfig.APP_KEY, JPushConfig.MASTER_SECRET);
        NettyHttpClient client = new NettyHttpClient(authorization, null, clientConfig);
        try {
            URI uri = new URI(host + clientConfig.get(ClientConfig.PUSH_PATH));
            client.sendRequest(HttpMethod.POST, pushPayload.toString(), uri, callback);
        } catch (URISyntaxException e) {
            log.error("jPush push message failed !", e);
        } finally {
            client.close();
        }
    }

    public static PushPayload newPushPayload(String content) {
        return newPushPayload(null, content, new HashMap<>());
    }

    public static PushPayload newPushPayload(String content, Map<String, String> extras) {
        return newPushPayload(null, content, extras);
    }

    public static PushPayload newPushPayload(String title, String content, Map<String, String> extras) {
        return defaultPushPayloadBuilder(title, content, extras)
                .setAudience(Audience.all())
                .build();
    }

    public static PushPayload newPushPayload(String title, String content, Map<String, String> extras, String ... regIds) {
        return newPushPayload(title, content, extras, new ArrayList<>(Arrays.asList(regIds)));
    }

    public static PushPayload newPushPayload(String title, String content, Map<String, String> extras, List<String> aliases) {
        aliases = aliases.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if(aliases.isEmpty()) {
            throw new IllegalArgumentException("registrationId is null !");
        }
        return defaultPushPayloadBuilder(title, content, extras)
                .setAudience(Audience.alias(aliases))
                .build();
    }

    public static PushPayload.Builder defaultPushPayloadBuilder(String title, String content, Map<String, String> extras) {
        extras = CollectionUtils.isEmpty(extras) ? new HashMap<>() : extras;
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setNotification(
                        Notification.newBuilder()
                                .setAlert(content)
                                .addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtras(extras).build())
                                .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(extras).build())
                                .build());
    }
}
