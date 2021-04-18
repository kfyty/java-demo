package com.kfyty.shiro.service;

import com.kfyty.shiro.config.ShiroConfig;
import com.kfyty.shiro.entity.User;
import com.kfyty.shiro.mapper.UserMapper;
import com.kfyty.shiro.utils.EncryptionUtil;
import com.kfyty.shiro.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionService permissionService;

    /**
     * 生成加密密码时，使用 用户名 + 盐值 作为最终盐值
     * 验证时，传入 用户名 + 盐值 最为实际盐值
     */
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
        return permissionService.findMenuByUserId(userId, 0);
    }
}
