package com.kfyty.netty.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述: msgType: test
 * 传输消息示例: {"msgType":"test","id":100,"name":"hello netty"}
 *
 * @author kfyty
 * @date 2021/1/8 17:48
 * @email kfyty725@hotmail.com
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class TestMessage extends BaseMessage {
    private Long id;
    private String name;
}
