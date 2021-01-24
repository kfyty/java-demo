package com.kfyty.redis.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/11/12 14:12
 * @email kfyty725@hotmail.com
 */
@Component
public class CustomerEventListener /* implements ApplicationListener<CustomerEvent> */ {
//    @Override
    @EventListener
    public void onApplicationEvent(CustomerEvent event) {
        System.out.println("listener: " + event.getSource());
    }
}
