package com.kfyty.security.config;

import com.kfyty.security.entity.Permission;
import com.kfyty.security.entity.Role;
import com.kfyty.security.entity.User;
import com.kfyty.security.mapper.PermissionMapper;
import com.kfyty.security.mapper.RoleMapper;
import com.kfyty.security.mapper.UserMapper;
import com.kfyty.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

@Configuration
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if(user == null) {
            return null;
        }
        return buildUserDetail(user);
    }

    public UserDetail buildUserDetail(User user) {
        UserDetail userDetail = new UserDetail(user);
        userDetail.getRoles().addAll(roleMapper.findByUserId(user.getId()).stream().map(Role::getName).collect(Collectors.toList()));
        userDetail.getPermissions().addAll(permissionMapper.findByUserId(user.getId()).stream().map(Permission::getUrl).collect(Collectors.toList()));
        userDetail.getMenus().addAll(userService.findMenuByUserId(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
        return userDetail;
    }
}
