package com.kfyty.dynamic.entity.sso;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private Date createTime;
    private Date updateTime;

    public static User newUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setSalt("salt");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return user;
    }
}
