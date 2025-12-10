package com.pig4cloud.pig.common.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.core.util.RetOps;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详情服务接口,扩展了Spring Security的UserDetailsService和Ordered接口
 * 提供用户详情加载、客户端支持校验及排序功能
 */
public interface PigUserDetailsService extends UserDetailsService, Ordered {

	/**
	 * 是否支持此客户端校验,目标客户端
	 */
	default boolean support(String clientId, String grantType) {
		return true;
	}

    /**
     *
     */
	default int getOrder() {
		return 0;
	}

	/**
	 * 根据用户信息构建UserDetails对象,包含用户信息的R对象,根据UserInfo构建好的UserDetails对象
	 */
	default UserDetails getUserDetails(Result<UserInfo> result) {
		UserInfo info = RetOps.of(result).getData().orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
		Set<String> dbAuthsSet = new HashSet<>();

		// 维护角色列表,添加role的前缀
		info.getRoleList().forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role.getRoleId()));

		// 维护权限列表
		dbAuthsSet.addAll(info.getPermissions());
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));

		// 构造security用户
		return new PigUser(info.getUserId(), info.getDept().getDeptId(), info.getUsername(),
				SecurityConstants.BCRYPT + info.getPassword(), info.getPhone(), true, true, true,
                CharSequenceUtil.equals(info.getLockFlag(), CommonConstants.STATUS_NORMAL), authorities);
	}

	/**
	 * 通过用户实体查询用户详情,用户实体对象,用户详情信息
	 */
	default UserDetails loadUserByUser(PigUser pigUser) {
		return loadUserByUsername(pigUser.getUsername());
	}

}
