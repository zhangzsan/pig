package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 部门关系表
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门关系")
public class SysDeptRelation extends Model<SysDeptRelation> {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 祖先节点
	 */
	@Schema(description = "祖先节点")
	private Long ancestor;

	/**
	 * 后代节点
	 */
	@Schema(description = "后代节点")
	private Long descendant;

}
