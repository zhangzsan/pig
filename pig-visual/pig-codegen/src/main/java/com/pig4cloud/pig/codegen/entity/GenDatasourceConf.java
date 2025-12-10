package com.pig4cloud.pig.codegen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 数据源表
 */
@Data
@TableName("gen_datasource_conf")
@EqualsAndHashCode(callSuper = true)
public class GenDatasourceConf extends Model<GenDatasourceConf> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 数据库类型
	 */
	private String dsType;

	/**
	 * 配置类型 (0主机形式 |1 url形式）
	 */
	private Integer confType;

	/**
	 * 主机地址
	 */
	private String host;

	/**
	 * 端口
	 */
	private Integer port;

	/**
	 * jdbc-url
	 */
	private String url;

	/**
	 * 实例
	 */
	private String instance;

	/**
	 * 数据库名称
	 */
	private String dsName;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

}
