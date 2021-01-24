package com.kfyty.shiro.service;

import com.kfyty.shiro.config.ShiroConfig;
import com.kfyty.shiro.entity.Permission;
import com.kfyty.shiro.entity.User;
import com.kfyty.shiro.mapper.UserMapper;
import com.kfyty.shiro.utils.EncryptionUtil;
import com.kfyty.shiro.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionService permissionService;

    public int registerUser(User user) {
        Objects.requireNonNull(user);
        if(userMapper.findByUsername(user.getUsername()) != null) {
            return 0;
        }
        user.setSalt(user.computeSalt());
        user.setPassword(EncryptionUtil.encryption(user.getPassword(), user.getUsername() + user.getSalt(), ShiroConfig.HASH_ITERATIONS));
        return userMapper.insert(user);
    }

    public List<MenuVo> findMenuByUserId(Integer userId) {
        Objects.requireNonNull(userId);
        List<MenuVo> result = new ArrayList<>();
        List<Permission> menus = permissionService.findByUserIdAndType(userId, "menu");
        for (Permission menu : menus) {
            result.add(permissionService.findMenuByUserIdAndPermission(userId, menu));
        }
        return result;
    }
}
