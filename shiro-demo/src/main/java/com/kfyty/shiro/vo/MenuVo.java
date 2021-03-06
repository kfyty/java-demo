package com.kfyty.shiro.vo;

import com.kfyty.shiro.entity.Permission;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private Permission menu;
    private List<MenuVo> children;
}
