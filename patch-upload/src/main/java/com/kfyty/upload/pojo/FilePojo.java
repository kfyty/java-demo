package com.kfyty.upload.pojo;

import java.util.Date;

import lombok.Data;

/**
 * TABLE_NAME: file
 * TABLE_COMMENT: 
 *
 * By kfyty
 */
@Data
public class FilePojo {
	/**
	 * 
	 */
	private Integer id;

	/**
	 *
	 */
	private Integer patchIndex;

	/**
	 * 
	 */
	private Integer parent;

	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 */
	private String path;

	/**
	 * 
	 */
	private String md5;

	/**
	 * 
	 */
	private Long size;

	/**
	 * 
	 */
	private Date createTime;

	public FilePojo() {

    }

	public FilePojo(Integer patchIndex, Integer parent, String name, String path, String md5, Long size) {
	    this.patchIndex = patchIndex;
		this.parent = parent;
		this.name = name;
		this.path = path;
		this.md5 = md5;
		this.size = size;
	}
}
