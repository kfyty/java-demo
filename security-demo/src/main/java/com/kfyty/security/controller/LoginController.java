package com.kfyty.security.controller;

import com.kfyty.security.config.security.UserDetail;
import com.kfyty.security.holder.UserHolder;
import com.kfyty.security.vo.MenuVo;
import com.kfyty.security.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @PostMapping("passport/login")
    public Result<String> login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationConfiguration.getAuthenticationManager().authenticate(authenticationToken);
            return Result.OK(((UserDetail) authenticate.getPrincipal()).getToken());
        } catch (Exception e) {
            log.error("login error !", e);
            return Result.FAIL(e.getMessage());
        }
    }

    @PostMapping("menu/list")
    public Result<List<MenuVo>> findMenus() {
        return Result.OK(UserHolder.currentUserDetail().getMenus());
    }
}
