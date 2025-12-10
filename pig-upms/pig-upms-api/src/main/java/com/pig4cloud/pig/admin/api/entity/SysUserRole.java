package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 用户角色表
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户角色")
public class SysUserRole extends Model<SysUserRole> {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 角色ID
	 */
	@Schema(description = "角色id")
	private Long roleId;

}
