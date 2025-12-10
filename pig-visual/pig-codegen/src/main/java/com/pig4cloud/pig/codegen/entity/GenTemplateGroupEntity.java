package com.pig4cloud.pig.codegen.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * 模板分组关联表
 */
@Data
@TableName("gen_template_group")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "模板分组关联表")
public class GenTemplateGroupEntity extends Model<GenTemplateGroupEntity> {

	/**
	 * 分组id
	 */
	@Schema(description = "分组id")
	private Long groupId;

	/**
	 * 模板id
	 */
	@Schema(description = "模板id")
	private Long templateId;

}
