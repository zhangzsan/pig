package com.pig4cloud.pig.codegen.entity;

import lombok.Data;

import java.util.List;

@Data
public class TableEntity {

	/**
	 * 名称
	 */
	private String tableName;

	/**
	 * 备注
	 */
	private String comments;

	/**
	 * 主键
	 */
	private ColumnEntity pk;

	/**
	 * 列名
	 */
	private List<ColumnEntity> columns;

	/**
	 * 驼峰类型
	 */
	private String caseClassName;

	/**
	 * 普通类型
	 */
	private String lowerClassName;

	/**
	 * 数据库类型(用于根据数据库个性化)
	 */
	private String dbType;

}
