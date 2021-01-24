package com.kfyty.dynamic;

import com.kfyty.dynamic.mapper.test.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@SpringBootApplication
public class DynamicDataSourceApplication {
    private static DynamicDataSourceApplication self;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    public void setSelf(DynamicDataSourceApplication self) {
        DynamicDataSourceApplication.self = self;
    }

//    @CrossOrigin
    @RequestMapping(value = "jsonp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String jsonp(HttpServletRequest request) {
        String callback_fun = request.getParameter("callback_fun_param");
        return callback_fun + "(" + "\"jsonp\"" + ")";
    }

    public static void main(String[] args) {
        SpringApplication.run(DynamicDataSourceApplication.class, args);
    }
}
