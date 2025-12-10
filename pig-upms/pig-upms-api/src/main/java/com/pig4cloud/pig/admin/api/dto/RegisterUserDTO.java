package com.pig4cloud.pig.admin.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 注册用户Dto
 */
@Setter
@Getter
public class RegisterUserDTO {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 新密码
	 */
	private String password;

	/**
	 * 电话
	 */
	private String phone;

}
