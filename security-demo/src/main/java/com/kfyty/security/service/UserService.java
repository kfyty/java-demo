package com.kfyty.security.service;

import com.kfyty.security.entity.User;
import com.kfyty.security.entity.UserRole;
import com.kfyty.security.exception.BusinessException;
import com.kfyty.security.holder.UserHolder;
import com.kfyty.security.mapper.RoleMapper;
import com.kfyty.security.mapper.UserMapper;
import com.kfyty.security.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleService roleService;

    public int registerUser(User user) {
        Objects.requireNonNull(user);
        if(userMapper.findByUsername(user.getUsername()) != null) {
            log.warn("the username already exists !");
            return 0;
        }
        user.setCreateTime(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void authorize(Integer userId, List<Integer> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        for (Integer roleId : roleIds) {
            if(!UserHolder.isRoot() && !this.roleService.hasRole(roleId)) {
                throw new BusinessException("你没有权限对该用户授权此角色：" + this.roleMapper.findByPk(roleId).getName());
            }
            userRoles.add(new UserRole(null, userId, roleId));
        }
        this.userRoleMapper.deleteByUserId(userId);
        this.userRoleMapper.insertAll(userRoles);
    }
}
