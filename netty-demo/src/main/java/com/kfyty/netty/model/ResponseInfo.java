package com.kfyty.netty.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kfyty.support.utils.JsonUtil;
import lombok.Data;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/9 11:05
 * @email kfyty725@hotmail.com
 */
@Data
public class ResponseInfo {
    private static final String FAILED = "FAILED";
    private static final String SUCCESS = "SUCCESS";

    private String status;
    private String message;

    public String toJson() {
        try {
            return JsonUtil.toJson(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String SUCCESS() {
        ResponseInfo info = new ResponseInfo();
        info.setStatus(SUCCESS);
        return info.toJson();
    }

    public static String FAILED(String msg) {
        ResponseInfo info = new ResponseInfo();
        info.setStatus(FAILED);
        info.setMessage(msg);
        return info.toJson();
    }
}
