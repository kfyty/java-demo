package com.kfyty.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TABLE_NAME: role_permission
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
