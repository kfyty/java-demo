package com.kfyty.security.entity;

import lombok.Data;

import java.util.Date;

/**
 * TABLE_NAME: role
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
public class Role {
	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 创建时间
	 */
	private Date createTime;

}
