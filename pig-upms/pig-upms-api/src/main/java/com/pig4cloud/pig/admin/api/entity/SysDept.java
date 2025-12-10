
package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 部门管理
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Schema(description = "部门")  //用于生成API文档，主要作用是描述数据模型和属性的元数据信息。
public class SysDept extends Model<SysDept> {

    @Serial
    private static final long serialVersionUID = 1L;

	@TableId(value = "dept_id", type = IdType.ASSIGN_ID)  //标识主键，并可以设置主键策略
	@Schema(description = "部门id")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	@Schema(description = "部门名称")
	private String name;

	/**
	 * 排序
	 */
	@NotNull(message = "排序值不能为空")
	@Schema(description = "排序值")
	private Integer sortOrder;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改人")
	private String updateBy;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 父级部门id
	 */
	@Schema(description = "父级部门id")
	private Long parentId;

	/**
	 * 是否删除 1：已删除 0：正常
	 */
	@TableLogic  //用于标记实体类中表示逻辑删除状态的字段，实现软删除而非物理删除。
	@Schema(description = "删除标记,1:已删除,0:正常")
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

}
