package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 种类管理
 */
@Data
@TableName("sys_category")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "种类管理")
public class SysCategory extends Model<SysCategory> {


	/**
	* 种类ID
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="种类ID")
    private Long categoryId;

	/**
	* 种类名称
	*/
    @Schema(description="种类名称")
    private String name;

	/**
	* 排序
	*/
    @Schema(description="排序")
    private Integer sortOrder;

	/**
	* 父级部门ID
	*/
    @Schema(description="父级部门ID")
    private Long parentId;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 修改人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改人")
    private String updateBy;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

	/**
	* 删除标志
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标志")
    private String delFlag;
}
