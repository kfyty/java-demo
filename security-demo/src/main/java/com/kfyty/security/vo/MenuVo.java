package com.kfyty.security.vo;

import com.kfyty.security.entity.Permission;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private Permission menu;
    private List<MenuVo> children;
}
