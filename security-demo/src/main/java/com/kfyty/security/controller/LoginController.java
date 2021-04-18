package com.kfyty.security.controller;


import com.kfyty.security.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @RequestMapping("passport/login")
    public Result<Void> login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationConfiguration.getAuthenticationManager().authenticate(authenticationToken);
            return Result.OK();
        } catch (Exception e) {
            log.error("login error !", e);
            return Result.FAIL(e.getMessage());
        }
    }
}
