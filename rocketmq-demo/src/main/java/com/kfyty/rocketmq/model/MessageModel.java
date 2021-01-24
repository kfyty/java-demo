package com.kfyty.rocketmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2021/1/14 17:29
 * @email kfyty725@hotmail.com
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
    private Integer id;
    private String name;
}
