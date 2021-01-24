package com.kfyty.shiro.advice;

import com.kfyty.shiro.vo.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionAdvice {
    @ResponseBody
    @ExceptionHandler
    public Result<Void> exceptionAdvice(Exception e) {
        if(e instanceof AuthorizationException) {
            return Result.FAIL(HttpStatus.UNAUTHORIZED, "权限不足！");
        }
        return Result.FAIL(e.getMessage());
    }
}
