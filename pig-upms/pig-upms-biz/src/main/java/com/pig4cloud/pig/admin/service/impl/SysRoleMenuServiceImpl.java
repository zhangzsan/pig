
package com.pig4cloud.pig.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysRoleMenu;
import com.pig4cloud.pig.admin.mapper.SysRoleMenuMapper;
import com.pig4cloud.pig.admin.service.SysRoleMenuService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;

import lombok.AllArgsConstructor;

/**
 * 角色菜单表服务实现类
 *
 */
@Service
@AllArgsConstructor
@SuppressWarnings("unused")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

	private final CacheManager cacheManager;

	/**
	 * @param roleId 角色
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId")
	public Boolean saveRoleMenus(Long roleId, String menuIds) {
		this.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, roleId));

		if (CharSequenceUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
		List<SysRoleMenu> roleMenuList = Arrays.stream(menuIds.split(StrPool.COMMA)).map(menuId -> {
			SysRoleMenu roleMenu = new SysRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(Long.valueOf(menuId));
			return roleMenu;
		}).toList();

		// 清空userinfo
		Objects.requireNonNull(cacheManager.getCache(CacheConstants.USER_DETAILS)).clear();
		this.saveBatch(roleMenuList);
		return Boolean.TRUE;
	}

}
