package com.kfyty.security.config;

import com.kfyty.security.exception.BusinessException;
import com.kfyty.security.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 20:04
 * @email kfyty725@hotmail.com
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler
    public Result<String> onBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return Result.FAIL(e.getMessage());
    }
}
