
package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysRoleMenu;

/**
 * 角色菜单表服务接口
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

	/**
     *
	 */
	Boolean saveRoleMenus(Long roleId, String menuIds);

}
