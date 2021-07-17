package com.kfyty.security.holder;

import com.kfyty.security.config.security.UserDetail;
import com.kfyty.security.constant.Constant;
import com.kfyty.security.entity.User;
import com.kfyty.security.exception.BusinessException;
import com.kfyty.security.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 16:19
 * @email kfyty725@hotmail.com
 */
@Slf4j
public class UserHolder {

    public static String currentToken() {
        String token = SpringContextHolder.currentRequest().getHeader(Constant.TOKEN);
        if(StringUtils.isEmpty(token)) {
            throw new BusinessException("token does not exist !");
        }
        return token;
    }

    public static User currentUser() {
        String token = currentToken();
        RedisService redisService = SpringContextHolder.getBean(RedisService.class);
        User user = redisService.hget(Constant.REDIS_USER_KEY.apply(token), User.class);
        return Optional.ofNullable(user).orElseThrow(() -> new BusinessException("登录已过期！"));
    }

    public static Integer currentUserId() {
        String token = currentToken();
        RedisService redisService = SpringContextHolder.getBean(RedisService.class);
        Integer userId = redisService.hget(Constant.REDIS_USER_KEY.apply(token), Constant.REDIS_USER_ID_KEY);
        return Optional.ofNullable(userId).orElseThrow(() -> new BusinessException("登录已过期！"));
    }

    public static UserDetail currentUserDetail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetail) {
            return (UserDetail) principal;
        }
        throw new BusinessException("登录已过期！");
    }

    public static boolean isRoot() {
        return currentUserDetail().getUser().isRoot();
    }
}
