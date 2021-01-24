package com.kfyty.shiro.service;

import com.kfyty.shiro.entity.Permission;
import com.kfyty.shiro.mapper.PermissionMapper;
import com.kfyty.shiro.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> findByUserIdAndType(Integer userId, String type) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(type);
        return permissionMapper.findByUserIdAndType(userId, type);
    }

    public MenuVo findMenuByUserIdAndPermission(Integer userId, Permission permission) {
        if(permission == null) {
            return null;
        }
        MenuVo menuVo = new MenuVo();
        menuVo.setMenu(permission);
        List<Permission> permissions = permissionMapper.findByUserIdAndPid(userId, permission.getId());
        if(CollectionUtils.isEmpty(permissions)) {
            return menuVo;
        }
        List<MenuVo> children = permissions.stream().map(e -> findMenuByUserIdAndPermission(userId, e)).collect(Collectors.toList());
        menuVo.setChildren(children);
        return menuVo;
    }
}
