package com.pig4cloud.pig.admin.api.dto;

import java.io.Serial;
import java.util.List;

import com.pig4cloud.pig.admin.api.entity.SysUser;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 */
@Setter
@Getter
@RequiredArgsConstructor
@Schema(description = "系统用户传输对象")
public class UserDTO extends SysUser {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
    @Schema(description = "角色id集合")
	private List<Long> role;

	/**
	 * 部门id
	 */
	@Schema(description = "部门id")
	private Long deptId;

	/**
	 * 岗位ID
	 */
	private List<Long> post;

	/**
	 * 新密码
	 */
    @Schema(description = "新密码")
	private String newpassword1;

}
