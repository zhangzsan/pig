package com.pig4cloud.pig.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode
@Schema(description = "前端角色展示对象")
public class RoleVO {

	/**
	 * 角色id
	 */
	private Long roleId;

	/**
	 * 菜单列表
	 */
	private String menuIds;

}
