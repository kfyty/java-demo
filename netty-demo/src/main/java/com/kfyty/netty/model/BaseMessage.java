package com.kfyty.netty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/8 17:45
 * @email kfyty725@hotmail.com
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMessage {
    protected String msgType;
    protected String originJson;
}
