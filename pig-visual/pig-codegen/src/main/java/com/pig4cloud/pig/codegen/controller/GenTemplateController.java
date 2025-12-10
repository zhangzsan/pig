package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pig.codegen.service.GenTemplateService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.xss.core.XssCleanIgnore;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板管理控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
@Tag(description = "template", name = "模板管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTemplateController {

	private final GenTemplateService genTemplateService;

	/**
	 * 分页查询模板信息
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("codegen_template_view")
	public Result<Page<GenTemplateEntity>> getTemplatePage(Page<GenTemplateEntity> page, GenTemplateEntity genTemplate) {
		LambdaQueryWrapper<GenTemplateEntity> wrapper = Wrappers.<GenTemplateEntity>lambdaQuery()
			.like(genTemplate.getId() != null, GenTemplateEntity::getId, genTemplate.getId())
			.like(StrUtil.isNotEmpty(genTemplate.getTemplateName()), GenTemplateEntity::getTemplateName,
					genTemplate.getTemplateName());
		return Result.ok(genTemplateService.page(page, wrapper));
	}

	/**
	 * 查询全部模板
	 */
	@Operation(summary = "查询全部", description = "查询全部")
	@GetMapping("/list")
	@HasPermission("codegen_template_view")
	public Result<List<GenTemplateEntity>> listTemplates() {
		return Result.ok(genTemplateService
			.list(Wrappers.<GenTemplateEntity>lambdaQuery().orderByDesc(GenTemplateEntity::getCreateTime)));
	}

	/**
	 * 通过id查询模板
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@HasPermission("codegen_template_view")
	public Result<GenTemplateEntity> getTemplateById(@PathVariable("id") Long id) {
		return Result.ok(genTemplateService.getById(id));
	}

	/**
	 * 新增模板
	 */
	@XssCleanIgnore
	@Operation(summary = "新增模板", description = "新增模板")
	@SysLog("新增模板")
	@PostMapping
	@HasPermission("codegen_template_add")
	public Result<Boolean> saveTemplate(@RequestBody GenTemplateEntity genTemplate) {
		return Result.ok(genTemplateService.save(genTemplate));
	}

	/**
	 * 修改模板
	 */
	@XssCleanIgnore
	@Operation(summary = "修改模板", description = "修改模板")
	@SysLog("修改模板")
	@PutMapping
	@HasPermission("codegen_template_edit")
	public Result<Boolean> updateTemplate(@RequestBody GenTemplateEntity genTemplate) {
		return Result.ok(genTemplateService.updateById(genTemplate));
	}

	/**
	 * 通过id删除模板
	 */
	@Operation(summary = "通过id删除模板", description = "通过id删除模板")
	@SysLog("通过id删除模板")
	@DeleteMapping
	@HasPermission("codegen_template_del")
	public Result<Boolean> removeTemplateByIds(@RequestBody Long[] ids) {
		return Result.ok(genTemplateService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("codegen_template_export")
	public List<GenTemplateEntity> exportTemplates(GenTemplateEntity genTemplate) {
		return genTemplateService.list(Wrappers.query(genTemplate));
	}

	/**
	 * 在线更新模板
	 */
	@Operation(summary = "在线更新模板", description = "在线更新模板")
	@GetMapping("/online")
	@HasPermission("codegen_template_view")
	public Result<Object> online() {
		return genTemplateService.onlineUpdate();
	}

	/**
	 * 检查版本
	 * @return {@link Result }
	 */
	@Operation(summary = "在线检查模板", description = "在线检查模板")
	@GetMapping("/checkVersion")
	@HasPermission("codegen_template_view")
	public Result<Boolean> checkVersion() {
		return genTemplateService.checkVersion();
	}

}
