package com.pig4cloud.pig.auth.support.core;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * OAuth2 Token 自定义增强实现类
 */
@Slf4j
public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

	/**
	 * 自定义OAuth 2.0 Token属性
	 */
	@Override
	public void customize(OAuth2TokenClaimsContext context) {
		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		claims.claim(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);
		String clientId = context.getAuthorizationGrant().getName();
        log.info("***********************客户端ID: {}", clientId);
		claims.claim(SecurityConstants.CLIENT_ID, clientId);
		if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
			return;
		}

		PigUser pigUser = (PigUser) context.getPrincipal().getPrincipal();
		claims.claim(SecurityConstants.DETAILS_USER, pigUser);
		claims.claim(SecurityConstants.DETAILS_USER_ID, pigUser.getId());
		claims.claim(SecurityConstants.USERNAME, pigUser.getUsername());
	}

}
