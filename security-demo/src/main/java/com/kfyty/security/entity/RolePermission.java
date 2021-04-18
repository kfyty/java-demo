package com.kfyty.security.entity;

import lombok.Data;

/**
 * TABLE_NAME: role_permission
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
public class RolePermission {
	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 角色id
	 */
	private Integer roleId;

	/**
	 * 许可id
	 */
	private Integer permissionId;

}
