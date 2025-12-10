package com.pig4cloud.pig.common.security.util;

import cn.hutool.core.text.CharSequenceUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类,获取当前登录的上下文信息
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    /**
	 * 获取当前安全上下文的认证信息,当前认证信息对象
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取当前认证用户,用户对象,如果认证主体不是PigUser类型则返回null
	 */
    public static PigUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof PigUser) {
            return (PigUser) principal;
        }
        return null;
    }

	/**
	 * 获取当前认证用户,当前认证用户对象,未认证时返回null
	 */
	public static PigUser getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息, 角色id集合
	 */
	public static List<Long> getRoles() {
        List<Long> roleIds = new ArrayList<>();
        Authentication authentication = getAuthentication();
        if(authentication == null){
            return roleIds;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		authorities.stream()
			.filter(granted -> CharSequenceUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
			.forEach(granted -> {
				String id = CharSequenceUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
				roleIds.add(Long.parseLong(id));
			});
		return roleIds;
	}

}
