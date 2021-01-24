package com.kfyty.jpush;

import com.kfyty.jpush.service.JPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author kfyty
 * @date 2020/12/14 11:45
 * @email kfyty725@hotmail.com
 */
@EnableAsync
@RestController
@SpringBootApplication
public class JPushApplication {
    @Autowired
    private JPushService jPushService;

    @GetMapping("push/{phone}")
    public String push(@PathVariable("phone") String phone, String content) {
        jPushService.pushMessage(phone, content);
        return "success";
    }

    public static void main(String[] args) {
        SpringApplication.run(JPushApplication.class, args);
    }
}
