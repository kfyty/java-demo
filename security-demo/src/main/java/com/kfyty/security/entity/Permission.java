package com.kfyty.security.entity;

import lombok.Data;

import java.util.Date;

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
	 * 创建时间
	 */
	private Date createTime;

}
