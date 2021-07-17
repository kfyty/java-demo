package com.kfyty.security.controller;

import com.kfyty.security.entity.User;
import com.kfyty.security.service.UserService;
import com.kfyty.security.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("register")
    public Result<Void> register(User user) {
        return Result.test(userService.registerUser(user) == 1, "注册失败！");
    }

    /**
     * 给用户授权角色
     * 只能给用户授权自己拥有的角色(及下级角色)
     */
    @PostMapping("authorize")
    public Result<Void> authorize(@NotNull Integer userId, @NotNull @Size(min = 1) @RequestParam List<Integer> roleIds) {
        this.userService.authorize(userId, roleIds);
        return Result.OK();
    }
}
