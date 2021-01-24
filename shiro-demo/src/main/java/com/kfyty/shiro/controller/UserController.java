package com.kfyty.shiro.controller;

import com.kfyty.shiro.entity.User;
import com.kfyty.shiro.service.UserService;
import com.kfyty.shiro.vo.MenuVo;
import com.kfyty.shiro.vo.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("register")
    public Result<Void> register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userService.registerUser(user) == 1 ? Result.OK("注册成功！") : Result.FAIL("注册失败！");
    }

    @RequestMapping("menu/list")
    public Result<List<MenuVo>> findMenu() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return Result.OK(userService.findMenuByUserId(user.getId()));
    }
}
