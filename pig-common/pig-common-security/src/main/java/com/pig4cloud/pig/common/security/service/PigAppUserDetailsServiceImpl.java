package com.pig4cloud.pig.common.security.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;

import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.Result;
/**
 * 手机号验证,mobile验证
 */
@RequiredArgsConstructor
@Slf4j
public class PigAppUserDetailsServiceImpl  implements PigUserDetailsService {

    private final RemoteUserService remoteUserService;

    private final CacheManager cacheManager;
    /**
     * 根据手机号加载用户信息,用户手机号
     */
    @Override
    public UserDetails loadUserByUsername(String phone) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(phone);
            if (wrapper != null) {
                Object cachedUser = wrapper.get();
                if (cachedUser instanceof PigUser) {
                    return (PigUser) cachedUser;
                }
            }
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(phone);
        Result<UserInfo> result = remoteUserService.info(userDTO);
        UserDetails userDetails = getUserDetails(result);
        if (cache != null) {
            cache.put(phone, userDetails);
        }
        return userDetails;
    }

    /**
     * 根据用户信息加载用户详情
     */
    @Override
    public UserDetails loadUserByUser(PigUser pigUser) {
        return loadUserByUsername(pigUser.getPhone());
    }

    /**
     * 是否支持此客户端校验
     */
    @Override
    public boolean support(String clientId, String grantType) {
        return SecurityConstants.MOBILE.equals(grantType);
    }

}
