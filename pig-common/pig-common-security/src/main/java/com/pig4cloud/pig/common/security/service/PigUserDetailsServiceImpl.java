package com.pig4cloud.pig.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;

import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.Result;
/**
 * 用户详情服务实现类,提供基于用户名加载用户详情功能
 */
@Primary
@RequiredArgsConstructor
public class PigUserDetailsServiceImpl implements PigUserDetailsService {

	private final RemoteUserService remoteUserService;

	private final CacheManager cacheManager;

    /**
	 * 根据用户名加载用户详情,用户名
	 */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(username);
            if (valueWrapper != null) {
                return (PigUser) valueWrapper.get();
            }
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        //通过用户名
        Result<UserInfo> result = remoteUserService.info(userDTO);
        UserDetails userDetails = getUserDetails(result);
        if (cache != null && userDetails != null) {
            cache.put(username, userDetails);
        }
        return userDetails;
    }

}
