package com.kfyty.shiro.controller;

import com.kfyty.shiro.service.RoleService;
import com.kfyty.shiro.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequiresPermissions("")
    @RequestMapping("role/add")
    public Result<Void> addRole(String role) {
        return Result.OK();
    }
}
