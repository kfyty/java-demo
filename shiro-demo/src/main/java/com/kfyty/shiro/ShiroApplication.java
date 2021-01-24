package com.kfyty.shiro;

import com.kfyty.shiro.annotation.NoRepeat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@RestController
@SpringBootApplication
public class ShiroApplication {
    private static final Supplier<SimpleDateFormat> sdf = () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @NoRepeat
    @RequestMapping("no-repeat")
    public String noRepeatTest(String id) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return sdf.get().format(new Date()) + ": " + id;
    }

    public static void main(String[] args) {
        SpringApplication.run(ShiroApplication.class, args);
    }
}
