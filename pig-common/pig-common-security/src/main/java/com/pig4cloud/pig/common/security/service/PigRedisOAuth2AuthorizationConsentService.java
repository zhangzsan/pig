package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.common.core.util.RedisUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的OAuth2授权同意服务实现。它实现了OAuth2AuthorizationConsentService接口,用于保存、删除和查找OAuth2授权同意信息
 * 缓存前缀 token:consent:
 */
@Slf4j
@NoArgsConstructor
public class PigRedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final Long TIMEOUT = 10L;

	/**
	 * 保存OAuth2授权同意信息
	 */
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		RedisUtils.set(buildKey(authorizationConsent), authorizationConsent, TIMEOUT, TimeUnit.MINUTES);
	}

	/**
	 * 移除OAuth2授权同意信息
	 */
	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		RedisUtils.delete(buildKey(authorizationConsent));
	}

	/**
	 * 根据注册客户端ID和主体名称查找OAuth2授权同意信息
	 * registeredClientId 注册客户端ID，不能为空
	 * principalName 主体名称,不能为空
	 */
	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return RedisUtils.get(buildKey(registeredClientId, principalName));
	}

	/**
	 * 构建授权确认信息的key
	 * registeredClientId 注册客户端ID
	 * principalName 主体名称
	 */
	private static String buildKey(String registeredClientId, String principalName) {
		String tokenStr = "token:consent:" + registeredClientId + ":" + principalName;
        log.info("********************8 tokenStr:{}", tokenStr);
        return tokenStr;
	}

	/**
	 * 构建授权同意的键值
	 * authorizationConsent 授权同意对象
	 */
	private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
		return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}
}
