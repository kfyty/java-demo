package com.kfyty.security.service;

import com.kfyty.security.entity.User;
import com.kfyty.security.mapper.UserMapper;
import com.kfyty.security.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public int registerUser(User user) {
        Objects.requireNonNull(user);
        if(userMapper.findByUsername(user.getUsername()) != null) {
            return 0;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }

    public List<MenuVo> findMenuByUserId(Integer userId) {
        Objects.requireNonNull(userId);
        return permissionService.findMenuByUserId(userId, 0);
    }
}
