package com.kfyty.dynamic.entity.test;

import lombok.Data;

import java.util.Date;

@Data
public class Test {
    private Integer id;
    private Date createTime;

    public static Test newTest() {
        Test test = new Test();
        test.setCreateTime(new Date());
        return test;
    }
}
