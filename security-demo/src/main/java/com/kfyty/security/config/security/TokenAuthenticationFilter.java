package com.kfyty.security.config.security;

import com.kfyty.security.constant.Constant;
import com.kfyty.security.entity.User;
import com.kfyty.security.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 13:27
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constant.TOKEN);
        if(StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        if (!this.redisService.exists(Constant.REDIS_USER_KEY.apply(token))) {
            context.setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }
        if(context.getAuthentication() == null || context.getAuthentication().getPrincipal() == null) {
            User user = this.redisService.hget(Constant.REDIS_USER_KEY.apply(token), User.class);
            if(user != null) {
                this.userDetailService.buildUserDetail(user, token);
            }
        }
        this.redisService.expire(Constant.REDIS_USER_KEY.apply(token), 30, TimeUnit.MINUTES);
        filterChain.doFilter(request, response);
    }
}
