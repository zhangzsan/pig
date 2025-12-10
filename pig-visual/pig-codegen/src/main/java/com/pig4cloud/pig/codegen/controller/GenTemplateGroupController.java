package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;
import com.pig4cloud.pig.codegen.entity.GenTemplateGroupEntity;
import com.pig4cloud.pig.codegen.service.GenTemplateGroupService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板分组关联表
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/templateGroup")
@Tag(description = "templateGroup", name = "模板分组关联表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTemplateGroupController {

	private final GenTemplateGroupService genTemplateGroupService;

	/**
	 * 分页查询
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("codegen_templateGroup_view")
	public Result<Page<GenTemplateGroupEntity>> getTemplateGroupPage(Page<GenTemplateGroupEntity> page, GenTemplateGroupEntity genTemplateGroup) {
		LambdaQueryWrapper<GenTemplateGroupEntity> wrapper = Wrappers.lambdaQuery();
		return Result.ok(genTemplateGroupService.page(page, wrapper));
	}

	/**
	 * 通过id查询模板分组关联表
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{groupId}")
	@HasPermission("codegen_templateGroup_view")
	public Result<GenTemplateGroupEntity> getTemplateGroupById(@PathVariable("groupId") Long groupId) {
		return Result.ok(genTemplateGroupService.getById(groupId));
	}

	/**
	 * 新增模板分组关联表
	 */
	@Operation(summary = "新增模板分组关联表", description = "新增模板分组关联表")
	@SysLog("新增模板分组关联表")
	@PostMapping
	@HasPermission("codegen_templateGroup_add")
	public Result<Boolean> saveTemplateGroup(@RequestBody GenTemplateGroupEntity genTemplateGroup) {
		return Result.ok(genTemplateGroupService.save(genTemplateGroup));
	}

	/**
	 * 修改模板分组关联表
	 */
	@Operation(summary = "修改模板分组关联表", description = "修改模板分组关联表")
	@SysLog("修改模板分组关联表")
	@PutMapping
	@HasPermission("codegen_templateGroup_edit")
	public Result<Boolean> updateTemplateGroup(@RequestBody GenTemplateGroupEntity genTemplateGroup) {
		return Result.ok(genTemplateGroupService.updateById(genTemplateGroup));
	}

	/**
	 * 通过id删除模板分组关联表
	 */
	@Operation(summary = "通过id删除模板分组关联表", description = "通过id删除模板分组关联表")
	@SysLog("通过id删除模板分组关联表")
	@DeleteMapping
	@HasPermission("codegen_templateGroup_del")
	public Result<Boolean> removeTemplateGroupByIds(@RequestBody Long[] ids) {
		return Result.ok(genTemplateGroupService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("codegen_templateGroup_export")
	public List<GenTemplateGroupEntity> exportTemplateGroups(GenTemplateGroupEntity genTemplateGroup) {
		return genTemplateGroupService.list(Wrappers.query(genTemplateGroup));
	}

}
