package com.kfyty.security.vo;

import com.kfyty.security.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleVo {
    private Role role;
    private List<RoleVo> children;

    public RoleVo(Role role) {
        this.role = role;
    }
}
