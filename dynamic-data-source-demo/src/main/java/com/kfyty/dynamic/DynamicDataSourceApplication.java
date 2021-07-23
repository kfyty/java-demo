package com.kfyty.dynamic;

import com.baidu.fsg.uid.UidGenerator;
import com.kfyty.dynamic.service.DynamicDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@SpringBootApplication
public class DynamicDataSourceApplication implements CommandLineRunner {
    @Value("${server.log}")
    private Boolean serverLog;

    @Value("${server.config}")
    private String serverConfig;

    @Autowired @Lazy
    private UidGenerator uidGenerator;

    @Autowired
    private DynamicDataSourceService service;

    /**
     * jsonp 测试，js 见 shiro-demo
     * 需要设置跨域
     * 建议使用过滤器，或者 @CrossOrigin 注解
     */
    @RequestMapping(value = "jsonp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String jsonp(HttpServletRequest request) {
        String callback_fun = request.getParameter("callback_fun_param");
        return callback_fun + "(" + "\"jsonp\"" + ")";
    }

    public static void main(String[] args) {
        SpringApplication.run(DynamicDataSourceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("server log: {}", serverLog);
        log.info("server config: {}", serverConfig);
        log.info("uid: {}", uidGenerator.getUID());
    }
}
