package com.kfyty.security.config.security;

import com.kfyty.security.constant.Constant;
import com.kfyty.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 12:26
 * @email kfyty725@hotmail.com
 */
@Component
public class LogoutRedisHandler implements LogoutHandler {
    @Autowired
    private RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader(Constant.TOKEN);
        if(!StringUtils.isEmpty(token)) {
            this.redisService.delete(Constant.REDIS_USER_KEY.apply(token));
        }
    }
}
