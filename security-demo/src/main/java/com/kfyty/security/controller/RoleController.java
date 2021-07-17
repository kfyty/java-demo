package com.kfyty.security.controller;

import com.kfyty.security.service.RoleService;
import com.kfyty.security.vo.Result;
import com.kfyty.security.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/17 18:28
 * @email kfyty725@hotmail.com
 */
@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("list")
    public Result<List<RoleVo>> findRoles() {
        return Result.OK(this.roleService.findRoles());
    }

    /**
     * 给角色授权许可
     * 只能给自己拥有的角色(及下级角色)，授权自己拥有的许可
     * @param roleId 角色 id
     * @param permissionIds 权限许可 id
     */
    @PostMapping("authorize")
    public Result<Void> authorize(@NotNull Integer roleId, @NotNull @Size(min = 1) @RequestParam List<Integer> permissionIds) {
        this.roleService.authorize(roleId, permissionIds);
        return Result.OK();
    }
}
