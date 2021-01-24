package com.kfyty.jpush.service;

import com.kfyty.jpush.utils.JPushUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/12/14 11:47
 * @email kfyty725@hotmail.com
 */
@Service
public class JPushService {
    @Async
    public void pushMessage(String phone, String content) {
        Map<String, String> param = new HashMap<>();
        param.put("orderNo", "123456780");
        JPushUtil.pushMessage(null, content, param, phone);
    }
}
