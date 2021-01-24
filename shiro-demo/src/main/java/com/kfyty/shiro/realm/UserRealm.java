package com.kfyty.shiro.realm;

import com.kfyty.shiro.entity.Permission;
import com.kfyty.shiro.entity.Role;
import com.kfyty.shiro.entity.User;
import com.kfyty.shiro.mapper.PermissionMapper;
import com.kfyty.shiro.mapper.RoleMapper;
import com.kfyty.shiro.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRealm extends AuthorizingRealm {
    @Lazy
    @Autowired
    private UserMapper userMapper;

    @Lazy
    @Autowired
    private RoleMapper roleMapper;

    @Lazy
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if(token.getPrincipal() == null || token.getCredentials() == null) {
            return null;
        }
        User user = userMapper.findByUsername(token.getPrincipal().toString());
        if(user == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getUsername() + user.getSalt()), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = (User) principal.getPrimaryPrincipal();
        List<Role> roles = roleMapper.findByUserId(user.getId());
        List<Permission> permissions = permissionMapper.findByUserIdAndType(user.getId(), "button");
        info.addRoles(roles.stream().map(Role::getName).collect(Collectors.toList()));
        info.addStringPermissions(permissions.stream().map(e -> e.getPermission() + ":" + e.getUrl()).collect(Collectors.toList()));
        return info;
    }
}
