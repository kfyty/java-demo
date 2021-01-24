package com.kfyty.shiro.controller;

import com.kfyty.shiro.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class LoginController {

    @ResponseBody
    @RequestMapping("passport/login")
    public Result<Void> login(String username, String password, String rememberMe) {
        boolean remember = rememberMe.equalsIgnoreCase("on");
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), remember);
        try {
            SecurityUtils.getSubject().login(token);
        } catch(UnknownAccountException e) {
            return Result.FAIL(HttpStatus.BAD_REQUEST, "账户不存在 !");
        } catch(IncorrectCredentialsException e) {
            return Result.FAIL(HttpStatus.BAD_REQUEST, "用户名或密码错误 !");
        } catch(Exception e) {
            log.error("user login error !", e);
            return Result.FAIL("服务器错误！");
        }
        return Result.OK();
    }
}
