package com.pig4cloud.pig.auth.support.sms;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.pig4cloud.pig.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationToken;

/**
 * 短信登录认证令牌
 */
public class OAuth2ResourceOwnerSmsAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	public OAuth2ResourceOwnerSmsAuthenticationToken(AuthorizationGrantType authorizationGrantType,
			Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
