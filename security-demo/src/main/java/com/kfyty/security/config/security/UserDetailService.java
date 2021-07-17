package com.kfyty.security.config.security;

import com.kfyty.security.constant.Constant;
import com.kfyty.security.entity.Permission;
import com.kfyty.security.entity.Role;
import com.kfyty.security.entity.User;
import com.kfyty.security.mapper.PermissionMapper;
import com.kfyty.security.mapper.RoleMapper;
import com.kfyty.security.mapper.UserMapper;
import com.kfyty.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        return user == null ? null : buildUserDetail(user);
    }

    public UserDetail buildUserDetail(User user) {
        return buildUserDetail(user, UUID.randomUUID().toString());
    }

    public UserDetail buildUserDetail(User user, String token) {
        List<Role> roles = user.isRoot() ? roleMapper.findAll() : roleMapper.findByUserId(user.getId());
        List<Permission> permissions = user.isRoot() ? permissionMapper.findAll() : permissionMapper.findByUserId(user.getId());
        UserDetail userDetail = new UserDetail(user, token, roles, permissions);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
        return userDetail;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        UserDetail userDetail = (UserDetail) event.getAuthentication().getPrincipal();
        User user = userDetail.getUser();
        user.setPassword(null);
        this.redisService.hset(Constant.REDIS_USER_KEY.apply(userDetail.getToken()), user, 30, TimeUnit.MINUTES);
    }
}
