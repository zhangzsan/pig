package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 令牌管理控制器：提供令牌的分页查询和删除功能
 * */
@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping("/sys-token")
@Tag(description = "token", name = "令牌管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTokenController {

	private final RemoteTokenService remoteTokenService;

	/**
	 * 获取分页token信息
	 * @param params 请求参数集合
	 * @return 包含token分页信息的响应结果
	 */
	@RequestMapping("/page")
	public Result<?> getTokenPage(@RequestBody Map<String, Object> params) {
		return remoteTokenService.getTokenPage(params);
	}

	/**
	 * 根据token数组删除token
	 * @param tokens 需要删除的token数组
	 */
	@SysLog("删除用户token")
	@DeleteMapping("/delete")
	@HasPermission("sys_token_del")
	public Result<Void> removeById(@RequestBody String[] tokens) {
		for (String token : tokens) {
			remoteTokenService.removeTokenById(token);
		}
		return Result.ok();
	}

}
