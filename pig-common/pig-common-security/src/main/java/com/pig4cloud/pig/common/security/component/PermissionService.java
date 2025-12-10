package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 接口权限判断工具类,判断是否有某种权限
 */
public class PermissionService {

    /**
     * 判断当前用户是否含有权限
     */
	public boolean hasPermission(String... permissions) {
		if (ArrayUtil.isEmpty(permissions)) {
			return false;
		}
        //获取当前登录者的权限信息
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authorities == null){
            return false;
        }
		return authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.filter(StringUtils::hasText)
			.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
	}

}
