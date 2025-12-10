package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.service.GenGroupService;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import com.pig4cloud.pig.codegen.util.vo.TemplateGroupDTO;
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
 * 模板分组管理控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(description = "group", name = "模板分组管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenGroupController {

	private final GenGroupService genGroupService;

	/**
	 * 分页查询模板分组
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("codegen_group_view")
	public Result<IPage<GenGroupEntity>> getGroupPage(Page<GenGroupEntity> page, GenGroupEntity genGroup) {
		LambdaQueryWrapper<GenGroupEntity> wrapper = Wrappers.<GenGroupEntity>lambdaQuery()
			.like(genGroup.getId() != null, GenGroupEntity::getId, genGroup.getId())
			.like(CharSequenceUtil.isNotEmpty(genGroup.getGroupName()), GenGroupEntity::getGroupName, genGroup.getGroupName());
		return Result.ok(genGroupService.page(page, wrapper));
	}

	/**
	 * 通过id查询模板分组
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@HasPermission("codegen_group_view")
	public Result<GroupVO> getGroupById(@PathVariable("id") Long id) {
		return Result.ok(genGroupService.getGroupVoById(id));
	}

	/**
	 * 新增模板分组
	 */
	@Operation(summary = "新增模板分组", description = "新增模板分组")
	@SysLog("新增模板分组")
	@PostMapping
	@HasPermission("codegen_group_add")
	public Result<Boolean> saveGroup(@RequestBody TemplateGroupDTO genTemplateGroup) {
		genGroupService.saveGenGroup(genTemplateGroup);
		return Result.ok();
	}

	/**
	 * 修改模板分组
	 */
	@Operation(summary = "修改模板分组", description = "修改模板分组")
	@SysLog("修改模板分组")
	@PutMapping
	@HasPermission("codegen_group_edit")
	public Result<Boolean> updateGroup(@RequestBody GroupVO groupVo) {
		genGroupService.updateGroupAndTemplateById(groupVo);
		return Result.ok();
	}

	/**
	 * 通过id删除模板分组
	 */
	@Operation(summary = "通过id删除模板分组", description = "通过id删除模板分组")
	@SysLog("通过id删除模板分组")
	@DeleteMapping
	@HasPermission("codegen_group_del")
	public Result<Void> removeGroupByIds(@RequestBody Long[] ids) {
		genGroupService.delGroupAndTemplate(ids);
		return Result.ok();
	}

	/**
	 * 导出excel 表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("codegen_group_export")
	public List<GenGroupEntity> exportGroups(GenGroupEntity genGroup) {
		return genGroupService.list(Wrappers.query(genGroup));
	}

	/**
	 * 查询列表
	 */
	@GetMapping("/list")
	@Operation(summary = "查询列表", description = "查询列表")
	public Result<List<GenGroupEntity>> listGroups() {
		List<GenGroupEntity> list = genGroupService
			.list(Wrappers.<GenGroupEntity>lambdaQuery().orderByDesc(GenGroupEntity::getCreateTime));
		return Result.ok(list);
	}

}
