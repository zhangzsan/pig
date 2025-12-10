package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 文件管理实体类
 *
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@Schema(description = "文件")
public class SysFile extends Model<SysFile> {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "文件编号")
	private Long id;

	/**
	 * 文件名
	 */
	@Schema(description = "文件名")
	private String fileName;

	/**
	 * 原文件名
	 */
	@Schema(description = "原始文件名")
	private String original;

	/**
	 * 容器名称
	 */
	@Schema(description = "存储桶名称")
	private String bucketName;

	/**
	 * 文件类型
	 */
	@Schema(description = "文件类型")
	private String type;

	/**
	 * 文件大小
	 */
	@Schema(description = "文件大小")
	private Long fileSize;

	/**
	 * 上传人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建者")
	private String createBy;

	/**
	 * 上传时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "更新者")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标识：1-删除，0-正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
