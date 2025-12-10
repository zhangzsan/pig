package com.pig4cloud.pig.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.common.core.util.Result;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限服务接口
 * 提供菜单权限相关的服务方法，包括查询、删除、更新和构建菜单树等操作
 *
 */
public interface SysMenuService extends IService<SysMenu> {

	/**
	 * 通过角色编号查询URL权限
	 */
	List<SysMenu> findMenuByRoleId(Long roleId);

	/**
	 * 级联删除菜单
	 */
	Result<Boolean> removeMenuById(Long id);

	/**
	 * 更新菜单信息
	 */
	Boolean updateMenuById(SysMenu sysMenu);

	/**
	 * 构建树查询
	 */
	List<Tree<Long>> getMenuTree(Long parentId, String menuName, String type);

	/**
	 * 查询菜单
	 */
	List<Tree<Long>> filterMenu(Set<SysMenu> all, String type, Long parentId);

}
