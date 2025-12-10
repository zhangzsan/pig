package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册控制器：提供用户注册功能
 */
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Tag(description = "register", name = "注册用户管理模块")
@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
@Slf4j
public class SysRegisterController {

	private final SysUserService userService;

	/**
	 * 注册用户
	 */
	@Inner(value = false)
	@SysLog("注册用户")
	@PostMapping("/user")
	public Result<Boolean> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        log.error("注册用户");
		return userService.registerUser(registerUserDTO);
	}

}
