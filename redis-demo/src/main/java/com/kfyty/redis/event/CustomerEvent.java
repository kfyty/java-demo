package com.kfyty.redis.event;

import org.springframework.context.ApplicationEvent;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 14:11
 * @email kfyty725@hotmail.com
 */
public class CustomerEvent extends ApplicationEvent {

    public CustomerEvent(Object source) {
        super(source);
    }
}
