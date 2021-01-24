package com.kfyty.table.struct.export.entity;

import java.util.Date;

import lombok.Data;

/**
 * TABLE_NAME: SYS_BO_LIST
 * TABLE_COMMENT: 系统自定义业务管理列表
 *
 * @author kfyty
 * @email kfyty725@hotmail.com
 */
@Data
public class SysBoList {
	/**
	 * ID
	 */
	private String id;

	/**
	 * 解决方案ID
	 */
	private String solId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 标识键
	 */
	private String key;

	/**
	 * 描述
	 */
	private String descp;

	/**
	 * 主键字段
	 */
	private String idField;

	/**
	 * 显示字段(树)
	 */
	private String textField;

	/**
	 * 父ID(树)
	 */
	private String parentField;

	/**
	 * 是否树对话框
	 */
	private String isTreeDlg;

	/**
	 * 仅可选择树节点
	 */
	private String onlySelLeaf;

	/**
	 * 数据地址
	 */
	private String url;

	/**
	 * 是否多选择
	 */
	private String multiSelect;

	/**
	 * 是否显示左树
	 */
	private String isLeftTree;

	/**
	 * 左树SQL，格式如"select * from abc"##"select * from abc2"
	 */
	private String leftNav;

	/**
	 * 左树字段映射
	 */
	private String leftTreeJson;

	/**
	 * SQL语句
	 */
	private String sql;

	/**
	 * 
	 */
	private String useCondSql;

	/**
	 * 
	 */
	private String condSqls;

	/**
	 * 数据源ID
	 */
	private String dbAs;

	/**
	 * 列字段JSON
	 */
	private String fieldsJson;

	/**
	 * 列的JSON
	 */
	private String colsJson;

	/**
	 * 列表显示模板
	 */
	private String listHtml;

	/**
	 * 搜索条件HTML
	 */
	private String searchJson;

	/**
	 * 绑定流程方案
	 */
	private String bpmSolId;

	/**
	 * 绑定表单方案
	 */
	private String formAlias;

	/**
	 * 头部按钮配置
	 */
	private String topBtnsJson;

	/**
	 * 脚本JS
	 */
	private String bodyScript;

	/**
	 * 是否对话框
	 */
	private String isDialog;

	/**
	 * 是否分页
	 */
	private String isPage;

	/**
	 * 是否允许导出
	 */
	private String isExport;

	/**
	 * 高
	 */
	private Long height;

	/**
	 * 宽
	 */
	private Long width;

	/**
	 * 是否启用流程
	 */
	private String enableFlow;

	/**
	 * 是否已产生HTML
	 */
	private String isGen;

	/**
	 * 是否共享
	 */
	private String isShare;

	/**
	 * 分类ID
	 */
	private String treeId;

	/**
	 * 脚本
	 */
	private String drawCellScript;

	/**
	 * 手机表单模板
	 */
	private String mobileHtml;

	/**
	 * 数据风格
	 */
	private String dataStyle;

	/**
	 * 行数据编辑
	 */
	private String rowEdit;

	/**
	 * 数据权限
	 */
	private String dataRightJson;

	/**
	 * 锁定开始列
	 */
	private Long startFroCol;

	/**
	 * 锁定结束列
	 */
	private Long endFroCol;

	/**
	 * 是否显示汇总行
	 */
	private String showSummaryRow;

	/**
	 * 租户ID
	 */
	private String tenantId;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 是否初始化数据
	 */
	private String isInitData;

	/**
	 * 明细表单
	 */
	private String formDetailAlias;

}
