package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.admin.service.SysLogService;
import com.pig4cloud.pig.common.core.util.Result;
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
 * 系统日志前端控制器
 *
 */
@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping("/log")
@Tag(description = "log", name = "日志管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysLogController {

	private final SysLogService sysLogService;

	/**
	 * 分页查询系统日志
	 * @param page 分页参数对象
	 * @param sysLog 系统日志查询条件
	 */
	@GetMapping("/page")
	public Result<?> getLogPage(@ParameterObject Page<SysLogDTO> page, @ParameterObject SysLogDTO sysLog) {
		return Result.ok(sysLogService.getLogPage(page, sysLog));
	}

	/**
	 * 批量删除日志
	 * @param ids 要删除的日志ID数组
	 */
	@DeleteMapping
	@HasPermission("sys_log_del")
	public Result<Boolean> removeByIds(@RequestBody Long[] ids) {
		return Result.ok(sysLogService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 保存日志
	 * @param sysLog 日志实体
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@Inner
	@PostMapping("/save")
	public Result<Boolean> saveLog(@Valid @RequestBody SysLog sysLog) {
		return Result.ok(sysLogService.saveLog(sysLog));
	}

	/**
	 * 导出系统日志到Excel表格
	 * @param sysLog 系统日志查询条件DTO
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_log_export")
	public List<SysLog> exportLogs(SysLogDTO sysLog) {
		return sysLogService.listLogs(sysLog);
	}

}
