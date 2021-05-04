package com.kfyty.netty.handler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/4/30 14:55
 * @email kfyty725@hotmail.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseHandlerModel {
    public static final String NETTY_HANDLER_PATH_MARK = "\"nettyPath\"";

    /**
     * 请求路径
     */
    private String nettyPath;

    @JsonIgnore
    private String originJson;
}
