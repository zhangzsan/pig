package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.vo.RoleExcelVO;
import com.pig4cloud.pig.admin.api.vo.RoleVO;
import com.pig4cloud.pig.admin.service.SysRoleService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器：提供角色相关的增删改查及权限管理功能
 */
@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Tag(description = "role", name = "角色管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysRoleController {

	private final SysRoleService sysRoleService;

	/**
	 * 通过ID查询角色信息
	 * @param id 角色ID
	 */
	@GetMapping("/details/{id}")
	public Result<SysRole> getById(@PathVariable Long id) {
		return Result.ok(sysRoleService.getById(id));
	}

	/**
	 * 查询角色详细信息
	 * @param query 角色查询条件对象
	 */
	@GetMapping("/details")
	public Result<SysRole> getDetails(@ParameterObject SysRole query) {
		return Result.ok(sysRoleService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 添加角色
	 * @param sysRole 角色信息
	 */
	@SysLog("添加角色")
	@PostMapping
	@HasPermission("sys_role_add")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public Result<Boolean> saveRole(@Valid @RequestBody SysRole sysRole) {
		return Result.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色信息
	 * @param sysRole 角色信息
	 */
	@SysLog("修改角色")
	@PutMapping
	@HasPermission("sys_role_edit")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public Result<Boolean> updateRole(@Valid @RequestBody SysRole sysRole) {
		return Result.ok(sysRoleService.updateById(sysRole));
	}

	/**
	 * 根据ID数组删除角色
	 * @param ids 角色ID数组
	 */
	@SysLog("删除角色")
	@DeleteMapping
	@HasPermission("sys_role_del")
	@CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
	public Result<Boolean> removeById(@RequestBody Long[] ids) {
		return Result.ok(sysRoleService.removeRoleByIds(ids));
	}

	/**
	 * 获取角色列表
	 */
	@GetMapping("/list")
	public Result<List<SysRole>> listRoles() {
		return Result.ok(sysRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询角色信息
	 * @param page 分页对象
	 * @param role 查询条件对象
	 */
	@GetMapping("/page")
	public Result<IPage<SysRole>> getRolePage(Page<SysRole> page, SysRole role) {
		return Result.ok(sysRoleService.page(page, Wrappers.<SysRole>lambdaQuery()
			.like(StrUtil.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())));
	}

	/**
	 * 更新角色菜单
	 * @param roleVo 角色VO对象
	 */
	@SysLog("更新角色菜单")
	@PutMapping("/menu")
	@HasPermission("sys_role_perm")
	public Result<Boolean> saveRoleMenus(@RequestBody RoleVO roleVo) {
		return Result.ok(sysRoleService.updateRoleMenus(roleVo));
	}

	/**
	 * 通过角色ID列表查询角色信息
	 * @param roleIdList 角色ID列表
	 */
	@PostMapping("/getRoleList")
	public Result<List<SysRole>> getRoleList(@RequestBody List<Long> roleIdList) {
		return Result.ok(sysRoleService.listRolesByRoleIds(roleIdList, CollUtil.join(roleIdList, StrUtil.UNDERLINE)));
	}

	/**
	 * 导出角色数据到Excel表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_role_export")
	public List<RoleExcelVO> exportRoles() {
		return sysRoleService.listRoles();
	}

	/**
	 * 导入角色
	 * @param excelVOList 角色Excel数据列表
	 * @param bindingResult 数据校验结果
	 */
	@PostMapping("/import")
	@HasPermission("sys_role_export")
	public Result<Object> importRole(@RequestExcel List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
		return sysRoleService.importRole(excelVOList, bindingResult);
	}

}
