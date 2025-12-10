package com.pig4cloud.pig.daemon.quartz.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.daemon.quartz.entity.SysJobLog;
import com.pig4cloud.pig.daemon.quartz.service.SysJobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务日志控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-job-log")
@Tag(description = "sys-job-log", name = "定时任务日志")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysJobLogController {

	private final SysJobLogService sysJobLogService;

	/**
	 * 分页查询定时任务日志
	 * @param page 分页对象
	 * @param sysJobLog 查询条件对象
	 */
	@GetMapping("/page")
	@Operation(description = "分页定时任务日志查询")
	public Result<IPage<SysJobLog>> getJobLogPage(Page<SysJobLog> page, SysJobLog sysJobLog) {
		return Result.ok(sysJobLogService.page(page, Wrappers.query(sysJobLog)));
	}

	/**
	 * 批量删除日志
	 * @param ids 要删除的日志ID数组
	 * @return 操作结果
	 */
	@DeleteMapping
	@Operation(description = "批量删除日志")
	public Result<Boolean> removeBatchByIds(@RequestBody Long[] ids) {
		return Result.ok(sysJobLogService.removeBatchByIds(CollUtil.toList(ids)));
	}

}
