package com.kfyty.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TABLE_NAME: user_role
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
