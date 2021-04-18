package com.kfyty.security.controller;

import com.kfyty.security.config.UserDetail;
import com.kfyty.security.entity.User;
import com.kfyty.security.service.UserService;
import com.kfyty.security.vo.MenuVo;
import com.kfyty.security.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.OK(userDetail.getMenus());
    }
}
