package com.pig4cloud.pig.auth.support.password;

import com.pig4cloud.pig.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

import static com.pig4cloud.pig.common.core.constant.SecurityConstants.PASSWORD;

/**
 * OAuth2 资源所有者密码认证提供者
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider
		extends OAuth2ResourceOwnerBaseAuthenticationProvider<OAuth2ResourceOwnerPasswordAuthenticationToken> {

	/**
	 * 使用提供的参数构造一个OAuth2ResourceOwnerPasswordAuthenticationProvider
	 */
	public OAuth2ResourceOwnerPasswordAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		super(authenticationManager, authorizationService, tokenGenerator);
	}

	/**
	 * 构建用户名密码认证令牌
	 * reqParameters 请求参数映射，包含用户名和密码
	 * 用户名密码认证令牌
	 */
	@Override
	public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
		String username = (String) reqParameters.get(OAuth2ParameterNames.USERNAME);
		String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	/**
	 * 判断是否支持指定的认证类型
	 * @param authentication 待验证的认证类型
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("********************88 supports authentication={} returning {}", authentication, supports);
		return supports;
	}

	/**
	 * 检查客户端是否支持密码授权模式
	 * registeredClient 已注册的客户端
	 */
	@Override
	public void checkClient(RegisteredClient registeredClient) {
		if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(PASSWORD))) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}
	}

}
