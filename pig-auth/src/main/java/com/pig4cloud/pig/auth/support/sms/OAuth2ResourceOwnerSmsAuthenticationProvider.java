package com.pig4cloud.pig.auth.support.sms;

import com.pig4cloud.pig.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * 短信登录的核心处理
 */
@Slf4j
public class OAuth2ResourceOwnerSmsAuthenticationProvider
		extends OAuth2ResourceOwnerBaseAuthenticationProvider<OAuth2ResourceOwnerSmsAuthenticationToken> {


	/**
	 * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
	 * provided parameters.
	 * authenticationManager
	 */
	public OAuth2ResourceOwnerSmsAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		super(authenticationManager, authorizationService, tokenGenerator);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = OAuth2ResourceOwnerSmsAuthenticationToken.class.isAssignableFrom(authentication);
        log.info("***********************supports authentication={} returning:{} ", authentication, supports);
		return supports;
	}

	@Override
	public void checkClient(RegisteredClient registeredClient) {
		if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.MOBILE))) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}
	}

	@Override
	public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
		String phone = (String) reqParameters.get(SecurityConstants.SMS_PARAMETER_NAME);
		return new UsernamePasswordAuthenticationToken(phone, null);
	}

}
