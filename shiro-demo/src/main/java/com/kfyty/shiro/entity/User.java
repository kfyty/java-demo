package com.kfyty.shiro.entity;

import com.kfyty.mybatis.auto.mapper.annotation.Column;
import com.kfyty.shiro.utils.EncryptionUtil;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.UUID;

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
	 * 盐值
	 */
	private String salt;

	/**
	 * 
	 */
	@Column(value = "CREATE_TIME", jdbcType = JdbcType.DATE)
	private Date createTime;

	public String computeSalt() {
		return EncryptionUtil.encryption(UUID.randomUUID().toString());
	}
}
