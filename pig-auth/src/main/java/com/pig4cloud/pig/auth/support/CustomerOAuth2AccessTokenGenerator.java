package com.pig4cloud.pig.auth.support;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.time.Instant;
import java.util.*;

/**
 * 自定义OAuth2访问令牌生成器
 */
public class CustomerOAuth2AccessTokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

	private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

    /**
     * 访问令牌生成器
     * 高安全性：使用Base64 URL安全的随机字符串生成器
     * 足够长度：96位(12字节)的随机令牌，抗暴力破解
     */
    private final StringKeyGenerator accessTokenGenerator = new Base64StringKeyGenerator(
			Base64.getUrlEncoder().withoutPadding(), 96);

	/**
     * 生成OAuth2访问令牌
     * 生成的访问令牌，如果令牌类型不是ACCESS_TOKEN或格式不是REFERENCE则返回null
     */
	@Nullable
	@Override
	public OAuth2AccessToken generate(OAuth2TokenContext context) {
		if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) || !OAuth2TokenFormat.REFERENCE
			.equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
			return null;
		}

		String issuer = null;
		if (context.getAuthorizationServerContext() != null) {
			issuer = context.getAuthorizationServerContext().getIssuer();
		}
		RegisteredClient registeredClient = context.getRegisteredClient();

		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

		// @formatter:off
        OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }
        claimsBuilder
                .subject(context.getPrincipal().getName())      // 用户主体
                .audience(Collections.singletonList(registeredClient.getClientId())) // 受众
                .issuedAt(issuedAt)        // 颁发时间
                .expiresAt(expiresAt)      // 过期时间
                .notBefore(issuedAt)       // 生效时间
                .id(UUID.randomUUID().toString());  // 唯一标识
        if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
            claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
        }
        // @formatter:on

       // 自定义声明定制(核心扩展点)
		if (this.accessTokenCustomizer != null) {
            OAuth2TokenClaimsContext.Builder accessTokenContextBuilder = OAuth2TokenClaimsContext.with(claimsBuilder)
                    .registeredClient(context.getRegisteredClient())
                    .principal(context.getPrincipal())
                    .authorizationServerContext(context.getAuthorizationServerContext())
                    .authorizedScopes(context.getAuthorizedScopes())
                    .tokenType(context.getTokenType())
                    .authorizationGrantType(context.getAuthorizationGrantType());
            if (context.getAuthorization() != null) {
                accessTokenContextBuilder.authorization(context.getAuthorization());
            }
            if (context.getAuthorizationGrant() != null) {
                accessTokenContextBuilder.authorizationGrant(context.getAuthorizationGrant());
            }
			OAuth2TokenClaimsContext accessTokenContext = accessTokenContextBuilder.build();
			this.accessTokenCustomizer.customize(accessTokenContext);
		}

		OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
        //生成认证
		return new CustomerOAuth2AccessTokenGenerator.OAuth2AccessTokenClaims(OAuth2AccessToken.TokenType.BEARER,
				this.accessTokenGenerator.generateKey(), accessTokenClaimsSet.getIssuedAt(),
				accessTokenClaimsSet.getExpiresAt(), context.getAuthorizedScopes(), accessTokenClaimsSet.getClaims());
	}

	/**
	 * 设置用于定制OAuth2AccessToken,OAuth2TokenClaimsContext#getClaims()}的 OAuth2TokenCustomizer}
	 * accessTokenCustomizer
	 */
	public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
		Assert.notNull(accessTokenCustomizer, "accessTokenCustomizer cannot be null");
		this.accessTokenCustomizer = accessTokenCustomizer;
	}

	/**
	 * OAuth2访问令牌声明类，继承自OAuth2AccessToken并实现ClaimAccessor接口
	 */
	private static final class OAuth2AccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {

		@Serial
		private static final long serialVersionUID = 1L;

		private final Map<String, Object> claims;

		/**
		 * 构造OAuth2访问令牌声明
		 */
		private OAuth2AccessTokenClaims(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt,
				Set<String> scopes, Map<String, Object> claims) {
			super(tokenType, tokenValue, issuedAt, expiresAt, scopes);
			this.claims = claims;
		}

		/**
		 * 获取claims集合,claims键值对集合
		 */
		@Override
		public Map<String, Object> getClaims() {
			return this.claims;
		}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OAuth2AccessTokenClaims that)) return false;
            if (!super.equals(o)) return false;
            return Objects.equals(claims, that.claims);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), claims);
        }

	}

}
