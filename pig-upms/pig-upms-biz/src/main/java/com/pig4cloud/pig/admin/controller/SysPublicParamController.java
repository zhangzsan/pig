package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysPublicParam;
import com.pig4cloud.pig.admin.service.SysPublicParamService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公共参数控制器：提供公共参数的增删改查及同步功能
 */
@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping("/param")
@Tag(description = "param", name = "公共参数配置")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysPublicParamController {

	private final SysPublicParamService sysPublicParamService;

	/**
	 * 根据key查询公共参数值
	 * @param publicKey 公共参数key
	 */
	@Inner(value = false)
	@Operation(description = "查询公共参数值", summary = "根据key查询公共参数值")
	@GetMapping("/publicValue/{publicKey}")
	public Result<String> publicKey(@PathVariable("publicKey") String publicKey) {
		return Result.ok(sysPublicParamService.getParamValue(publicKey));
	}

	/**
	 * 分页查询系统公共参数
	 * @param page 分页对象
	 * @param sysPublicParam 公共参数查询条件
	 */
	@Operation(description = "分页查询", summary = "分页查询")
	@GetMapping("/page")
	public Result<IPage<SysPublicParam>> getParamPage(@ParameterObject Page<SysPublicParam> page, @ParameterObject SysPublicParam sysPublicParam) {
		LambdaUpdateWrapper<SysPublicParam> wrapper = Wrappers.<SysPublicParam>lambdaUpdate()
			.like(StrUtil.isNotBlank(sysPublicParam.getPublicName()), SysPublicParam::getPublicName,
					sysPublicParam.getPublicName())
			.like(StrUtil.isNotBlank(sysPublicParam.getPublicKey()), SysPublicParam::getPublicKey,
					sysPublicParam.getPublicKey())
			.eq(StrUtil.isNotBlank(sysPublicParam.getSystemFlag()), SysPublicParam::getSystemFlag,
					sysPublicParam.getSystemFlag());

		return Result.ok(sysPublicParamService.page(page, wrapper));
	}

	/**
	 * 通过id查询公共参数
	 * @param publicId 公共参数id
	 */
	@Operation(description = "通过id查询公共参数", summary = "通过id查询公共参数")
	@GetMapping("/details/{publicId}")
	public Result<SysPublicParam> getById(@PathVariable("publicId") Long publicId) {
		return Result.ok(sysPublicParamService.getById(publicId));
	}

	/**
	 * 获取系统公共参数详情
	 * @param param 系统公共参数查询对象
	 */
	@GetMapping("/details")
	public Result<SysPublicParam> getDetail(@ParameterObject SysPublicParam param) {
		return Result.ok(sysPublicParamService.getOne(Wrappers.query(param), false));
	}

	/**
	 * 新增公共参数
	 * @param sysPublicParam 公共参数对象
	 */
	@Operation(description = "新增公共参数", summary = "新增公共参数")
	@SysLog("新增公共参数")
	@PostMapping
	@HasPermission("sys_syspublicparam_add")
	public Result<Boolean> saveParam(@RequestBody SysPublicParam sysPublicParam) {
		return Result.ok(sysPublicParamService.save(sysPublicParam));
	}

	/**
	 * 修改公共参数
	 * @param sysPublicParam 公共参数对象
	 * @return 操作结果
	 */
	@Operation(description = "修改公共参数", summary = "修改公共参数")
	@SysLog("修改公共参数")
	@PutMapping
	@HasPermission("sys_syspublicparam_edit")
	public Result<Boolean> updateParam(@RequestBody SysPublicParam sysPublicParam) {
		return sysPublicParamService.updateParam(sysPublicParam);
	}

	/**
	 * 通过id数组删除公共参数
	 * @param ids 要删除的公共参数id数组
	 * @return 操作结果
	 */
	@Operation(description = "删除公共参数", summary = "删除公共参数")
	@SysLog("删除公共参数")
	@DeleteMapping
	@HasPermission("sys_syspublicparam_del")
	public Result<Boolean> removeById(@RequestBody Long[] ids) {
		return sysPublicParamService.removeParamByIds(ids);
	}

	/**
	 * 导出excel 表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_syspublicparam_edit")
	public List<SysPublicParam> exportParams() {
		return sysPublicParamService.list();
	}

	/**
	 * 同步参数到缓存
	 * @return 操作结果
	 */
	@SysLog("同步参数")
	@PutMapping("/sync")
	@HasPermission("sys_syspublicparam_edit")
	public Result<Void> syncParam() {
		return sysPublicParamService.syncParamCache();
	}

}
