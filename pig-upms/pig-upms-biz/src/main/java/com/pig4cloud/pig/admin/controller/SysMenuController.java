package com.pig4cloud.pig.admin.controller;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.hutool.core.lang.tree.Tree;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.admin.service.SysMenuService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.util.SecurityUtils;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * 菜单管理控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Tag(description = "menu", name = "菜单管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMenuController {

	private final SysMenuService sysMenuService;

	/**
	 * 获取当前用户的树形菜单集合
	 * @param type 菜单类型
	 * @param parentId 父菜单ID
	 */
	@GetMapping
	public Result<List<Tree<Long>>> getUserMenu(String type, Long parentId) {
		// 获取符合条件的菜单
		Set<SysMenu> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
		return Result.ok(sysMenuService.filterMenu(all, type, parentId));
	}

	/**
	 * 获取树形菜单集合
	 * @param parentId 父节点ID
	 * @param menuName 菜单名称
	 * @param type 菜单类型
	 * @return 包含树形菜单的响应结果
	 */
	@GetMapping(value = "/tree")
	public Result<List<Tree<Long>>> getMenuTree(Long parentId, String menuName, String type) {
		return Result.ok(sysMenuService.getMenuTree(parentId, menuName, type));
	}

	/**
	 * 根据角色ID获取菜单树
	 * @param roleId 角色ID
	 * @return 包含菜单ID列表的响应结果
	 */
	@GetMapping("/tree/{roleId}")
	public Result<List<Long>> getRoleTree(@PathVariable Long roleId) {
		return Result.ok(sysMenuService.findMenuByRoleId(roleId).stream().map(SysMenu::getMenuId).toList());
	}

	/**
	 * 通过ID查询菜单的详细信息
	 * @param id 菜单ID
	 * @return 包含菜单详细信息的响应对象
	 */
	@GetMapping("/{id}")
	public Result<SysMenu> getById(@PathVariable Long id) {
		return Result.ok(sysMenuService.getById(id));
	}

	/**
	 * 新增菜单
	 * @param sysMenu 菜单信息
	 */
	@SysLog("新增菜单")
	@PostMapping
	@HasPermission("sys_menu_add")
	public Result<Boolean> saveMenu(@Valid @RequestBody SysMenu sysMenu) {
        boolean save = sysMenuService.save(sysMenu);
        return Result.ok(save);
	}

	/**
	 * 根据菜单ID删除菜单
	 * @param id 要删除的菜单ID
	 * @return 操作结果，成功返回success，失败返回false
	 */
	@SysLog("删除菜单")
	@DeleteMapping("/{id}")
	@HasPermission("sys_menu_del")
	public Result<Boolean> removeById(@PathVariable Long id) {
		return sysMenuService.removeMenuById(id);
	}

	/**
	 * 更新菜单信息
	 * @param sysMenu 菜单对象
	 * @return 操作结果
	 */
	@SysLog("更新菜单")
	@PutMapping
	@HasPermission("sys_menu_edit")
	public Result<Boolean> updateMenu(@Valid @RequestBody SysMenu sysMenu) {
		return Result.ok(sysMenuService.updateMenuById(sysMenu));
	}

}
