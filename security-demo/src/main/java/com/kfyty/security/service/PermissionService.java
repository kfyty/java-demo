package com.kfyty.security.service;

import com.kfyty.security.entity.Permission;
import com.kfyty.security.mapper.PermissionMapper;
import com.kfyty.security.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public List<MenuVo> findMenuByUserId(Integer userId, Integer pid) {
        List<Permission> permissions = permissionMapper.findByUserIdAndPid(userId, pid);
        if(CollectionUtils.isEmpty(permissions)) {
            return new LinkedList<>();
        }
        List<MenuVo> menuVos = new ArrayList<>();
        for (Permission permission : permissions) {
            MenuVo menu = new MenuVo();
            menu.setMenu(permission);
            menu.setChildren(this.findMenuByUserId(userId, permission.getId()));
            menuVos.add(menu);
        }
        return menuVos;
    }
}
