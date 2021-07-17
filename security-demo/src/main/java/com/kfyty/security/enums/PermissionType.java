package com.kfyty.security.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * 描述: 权限许可类型
 *
 * @author kfyty725
 * @date 2021/7/17 11:54
 * @email kfyty725@hotmail.com
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PermissionType {
    VIEW("V"),
    MENU("M"),
    API("API"),
    BUTTON("B"),
    ;

    private final String type;

    public String get() {
        return this.type;
    }
}
