
package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户端管理模块前端控制器
 *
 */
@RestController       //Controller和ResponseBody注解
@AllArgsConstructor
@RequestMapping("/client")
@Tag(description = "client", name = "客户端管理模块")  //OpenAPI 3.0 注解,用于API文档生成
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)  //所有接口都需要在请求头中包含 Authorization字段(JWT Token等)
public class SysClientController {

	private SysOauthClientDetailsService clientDetailsService;

	/**
	 * 通过客户端ID查询客户端详情，返回的data可能为null,会抛出异常
	 */
	@GetMapping("/{clientId}")
	public Result<SysOauthClientDetails> getByClientId(@PathVariable String clientId) {
		SysOauthClientDetails details = clientDetailsService
			.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId));
		return Result.ok(details);
	}

	/**
	 * 分页查询系统终端信息
	 */
	@GetMapping("/page")
	public Result<Page<SysOauthClientDetails>> getClientPage(@ParameterObject Page<SysOauthClientDetails> page, @ParameterObject SysOauthClientDetails sysOauthClientDetails) {
		LambdaQueryWrapper<SysOauthClientDetails> wrapper = Wrappers.<SysOauthClientDetails>lambdaQuery()
			.like(CharSequenceUtil.isNotBlank(sysOauthClientDetails.getClientId()), SysOauthClientDetails::getClientId,
					sysOauthClientDetails.getClientId())
			.like(CharSequenceUtil.isNotBlank(sysOauthClientDetails.getClientSecret()), SysOauthClientDetails::getClientSecret,
					sysOauthClientDetails.getClientSecret());
		return Result.ok(clientDetailsService.page(page, wrapper));
	}

	/**
	 * 添加客户端终端
	 */
	@SysLog("添加终端")
	@PostMapping
	@HasPermission("sys_client_add")   //校验必须有当前权限才能执行操作  使用@Vaiid校验hibernate valid实现
	public Result<Boolean> saveClient(@Valid @RequestBody SysOauthClientDetails clientDetails) {
		return Result.ok(clientDetailsService.saveClient(clientDetails));
	}

	/**
	 * 根据ID列表批量删除终端
	 */
	@SysLog("删除终端")
	@DeleteMapping
	@HasPermission("sys_client_del")
	public Result<Void> removeById(@RequestBody Long[] ids) {
		clientDetailsService.removeBatchByIds(CollUtil.toList(ids));
		return Result.ok();
	}

	/**
	 * 编辑终端信息
	 */
	@SysLog("编辑终端")
	@PutMapping
	@HasPermission("sys_client_edit")    //@Valid 是Java bean的验证
	public Result<Boolean> updateClient(@Valid @RequestBody SysOauthClientDetails clientDetails) {
		return Result.ok(clientDetailsService.updateClientById(clientDetails));
	}

	/**
     *
	 */
	@Inner
	@GetMapping("/getClientDetailsById/{clientId}")
	public Result<SysOauthClientDetails> getClientDetailsById(@PathVariable String clientId) {
		return Result.ok(clientDetailsService.getOne(
				Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId), false));
	}

	/**
	 * 同步缓存字典
	 */
	@SysLog("同步终端")
	@PutMapping("/sync")
	public Result<Void> syncClient() {
		return clientDetailsService.syncClientCache();
	}

	/**
	 * 导出符合条件的客户端信息到Excel
     */
	@ResponseExcel
	@SysLog("导出excel")
	@GetMapping("/export")
	public List<SysOauthClientDetails> exportClients(SysOauthClientDetails sysOauthClientDetails) {
		return clientDetailsService.list(Wrappers.query(sysOauthClientDetails));
	}

}
