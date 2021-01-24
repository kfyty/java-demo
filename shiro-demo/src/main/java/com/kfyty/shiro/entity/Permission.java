package com.kfyty.shiro.entity;

import java.util.Date;

import lombok.Data;

/**
 * TABLE_NAME: permission
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
public class Permission {
	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 许可名称
	 */
	private String name;

	/**
	 * url
	 */
	private String url;

	/**
	 * 许可类型
	 */
	private String type;

	/**
	 * 父id
	 */
	private Integer pid;

	/**
	 * 许可证
	 */
	private String permission;

	/**
	 * 创建时间
	 */
	private Date createTime;

}
