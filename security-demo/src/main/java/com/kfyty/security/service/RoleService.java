package com.kfyty.security.service;

import com.kfyty.security.config.security.UserDetail;
import com.kfyty.security.entity.Permission;
import com.kfyty.security.entity.Role;
import com.kfyty.security.entity.RolePermission;
import com.kfyty.security.exception.BusinessException;
import com.kfyty.security.holder.UserHolder;
import com.kfyty.security.mapper.PermissionMapper;
import com.kfyty.security.mapper.RoleMapper;
import com.kfyty.security.mapper.RolePermissionMapper;
import com.kfyty.security.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    public boolean hasRole(Integer roleId) {
        return this.hasRole(roleId, true);
    }

    public boolean hasRole(Integer roleId, boolean recursion) {
        UserDetail userDetail = UserHolder.currentUserDetail();
        return this.hasRole(userDetail.getRoles(), roleId, recursion);
    }

    public List<RoleVo> findRoles() {
        return UserDetail.buildRole(this.roleMapper.findAll(), null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void authorize(Integer roleId, List<Integer> permissionIds) {
        if(!UserHolder.isRoot() && !this.hasRole(roleId)) {
            throw new BusinessException("你没有权限对此角色授权！");
        }
        List<RolePermission> permissions = new ArrayList<>();
        Set<Integer> userPermissions = UserHolder.currentUserDetail().getPermissions().stream().map(Permission::getId).collect(Collectors.toSet());
        for (Integer permissionId : permissionIds) {
            if(!UserHolder.isRoot() && !userPermissions.contains(permissionId)) {
                throw new BusinessException("你没有权限授权此权限：" + this.permissionMapper.findByPk(permissionId).getName());
            }
            permissions.add(new RolePermission(null, roleId, permissionId));
        }
        this.rolePermissionMapper.deleteByRoleId(roleId);
        this.rolePermissionMapper.insertAll(permissions);
    }

    private boolean hasRole(Collection<Role> roles, Integer roleId, boolean recursion) {
        if(roles.stream().anyMatch(e -> e.getId().equals(roleId))) {
            return true;
        }
        if(recursion) {
            for (Role role : roles) {
                if(this.hasRole(this.roleMapper.findByPid(role.getId()), roleId, true)) {
                    return true;
                }
            }
        }
        return false;
    }
}
