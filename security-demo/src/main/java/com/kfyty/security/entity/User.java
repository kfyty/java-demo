package com.kfyty.security.entity;

import com.kfyty.mybatis.auto.mapper.annotation.Column;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.Objects;

/**
 * TABLE_NAME: user
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
public class User {
	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 
	 */
	@Column(value = "CREATE_TIME", jdbcType = JdbcType.DATE)
	private Date createTime;

	public boolean isRoot() {
		return Objects.equals(this.username, "root");
	}
}
