package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.vo.RoleExcelVO;
import com.pig4cloud.pig.admin.api.vo.RoleVO;
import com.pig4cloud.pig.common.core.util.Result;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * 系统角色服务接口
 * 提供角色相关的业务功能，包括角色查询、删除、更新菜单及导入导出等操作
 */
@SuppressWarnings("unused")
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 通过用户ID查询角色信息
	 */
	List<SysRole> listRolesByUserId(Long userId);

	/**
	 * 根据角色ID列表查询角色信息
	 */
	List<SysRole> listRolesByRoleIds(List<Long> roleIdList, String key);

	/**
	 * 通过角色ID数组删除角色
	 */
	Boolean removeRoleByIds(Long[] ids);

	/**
	 * 更新角色菜单列表
	 */
	Boolean updateRoleMenus(RoleVO roleVo);

	/**
	 * 导入角色
	 */
	Result<Object> importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 查询全部角色列表
	 */
	List<RoleExcelVO> listRoles();

}
