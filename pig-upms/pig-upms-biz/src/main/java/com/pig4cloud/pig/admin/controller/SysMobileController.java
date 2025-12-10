
package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.service.SysMobileService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手机管理模块控制器：提供手机验证码相关服务
 *
 */
@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
@Tag(description = "mobile", name = "手机管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMobileController {

	private final SysMobileService mobileService;

	/**
	 * 发送短信验证码
	 */
	@Inner(value = false)
	@GetMapping("/{mobile}")
	public Result<Boolean> sendSmsCode(@PathVariable String mobile) {
		return mobileService.sendSmsCode(mobile);
	}

}
