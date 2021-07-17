package com.kfyty.security.config.security;

import com.kfyty.security.entity.Permission;
import com.kfyty.security.entity.Role;
import com.kfyty.security.entity.User;
import com.kfyty.security.vo.MenuVo;
import com.kfyty.security.vo.RoleVo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class UserDetail implements UserDetails {
    private final User user;
    private final String token;
    private final List<Role> roles;
    private final List<Permission> permissions;
    private final List<MenuVo> menus;

    public UserDetail(User user, String token, List<Role> roles, List<Permission> permissions) {
        this.user = Objects.requireNonNull(user);
        this.token = Objects.requireNonNull(token);
        this.roles = roles;
        this.permissions = permissions;
        this.menus = buildMenu(permissions, null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(String role) {
        return this.roles.stream().anyMatch(e -> e.getName().equals(role));
    }

    public boolean hasPermission(String uri) {
        return this.permissions.stream().anyMatch(e -> Objects.equals(e.getUrl(), uri));
    }

    public static List<RoleVo> buildRole(List<Role> roles, RoleVo parent) {
        final int pid = parent == null ? 0 : parent.getRole().getId();
        return roles.stream()
                .filter(e -> e.getPid() == pid)
                .map(RoleVo::new)
                .peek(e -> e.setChildren(buildRole(roles, e)))
                .collect(Collectors.toList());
    }

    public static List<MenuVo> buildMenu(List<Permission> permissions, MenuVo parent) {
        final int pid = parent == null ? 0 : parent.getMenu().getId();
        return permissions.stream()
                .filter(e -> e.getPid() == pid)
                .map(MenuVo::new)
                .peek(e -> e.setChildren(buildMenu(permissions, e)))
                .collect(Collectors.toList());
    }
}
