package com.pig4cloud.pig.admin.api.entity;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志表
 */
@Setter
@Getter
@Schema(description = "日志")
public class SysLog implements Serializable {

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ExcelProperty("日志编号")  // EasyExcel库中的核心注解,用于定义Excel文件与Java对象之间的映射关系
	@Schema(description = "日志编号")
	private Long id;

	/**
	 * 日志类型
	 */
	@NotBlank(message = "日志类型不能为空")
	@ExcelProperty("日志类型（0-正常 9-错误）")
	@Schema(description = "日志类型")
	private String logType;

	/**
	 * 日志标题
	 */
	@NotBlank(message = "日志标题不能为空")
	@ExcelProperty("日志标题")
	@Schema(description = "日志标题")
	private String title;

	/**
	 * 创建者
	 */
	@ExcelProperty("创建人")
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ExcelIgnore
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 操作IP地址
	 */
	@ExcelProperty("操作ip地址")
	@Schema(description = "操作ip地址")
	private String remoteAddr;

	/**
	 * 用户代理
	 */
	@Schema(description = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@ExcelProperty("浏览器")
	@Schema(description = "请求uri")
	private String requestUri;

	/**
	 * 操作方式
	 */
	@ExcelProperty("操作方式")
	@Schema(description = "操作方式")
	private String method;

	/**
	 * 操作提交的数据
	 */
	@ExcelProperty("提交数据")
	@Schema(description = "提交数据")
	private String params;

	/**
	 * 执行时间
	 */
	@ExcelProperty("执行时间")
	@Schema(description = "方法执行时间")
	private Long time;

	/**
	 * 异常信息
	 */
	@ExcelProperty("异常信息")
	@Schema(description = "异常信息")
	private String exception;

	/**
	 * 服务ID
	 */
	@ExcelProperty("应用标识")
	@Schema(description = "应用标识")
	private String serviceId;

	/**
	 * 删除标记
	 */
	@TableLogic
	@ExcelIgnore
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;
}
