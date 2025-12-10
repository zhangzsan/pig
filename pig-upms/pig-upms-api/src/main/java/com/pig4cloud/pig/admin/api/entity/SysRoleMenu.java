package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

/**
 * 角色菜单表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "角色菜单")
public class SysRoleMenu extends Model<SysRoleMenu> {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@Schema(description = "角色id")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@Schema(description = "菜单id")
	private Long menuId;

}
