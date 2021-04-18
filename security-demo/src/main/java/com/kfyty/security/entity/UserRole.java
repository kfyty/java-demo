package com.kfyty.security.entity;

import lombok.Data;

/**
 * TABLE_NAME: user_role
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
public class UserRole {
	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 角色id
	 */
	private Integer roleId;

}
