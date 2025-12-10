package com.pig4cloud.pig.common.security.component;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 自定义的不透明令牌(Opaque Token)内省器，用于在资源服务器端验证令牌并获取认证主体信息。
 不透明令牌通常是指不包含用户信息(如JWT)的随机字符串,需要通过授权服务器的内省端点来验证令牌的有效性和获取用户信息。
 */
@Slf4j
@RequiredArgsConstructor
public class PigCustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	/**
	 * 作用：用于查询令牌对应的授权信息
     * 数据源：通常是Redis或数据库存储的授权记录
	 */
	private final OAuth2AuthorizationService authorizationService;

	/**
	 * 根据token内省获取认证主体信息
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 1. 验证令牌有效性
		OAuth2Authorization oldAuthorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (Objects.isNull(oldAuthorization)) {
			throw new InvalidBearerTokenException(token);
		}

        // 2. 处理客户端凭证模式
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(oldAuthorization.getAuthorizationGrantType())) {
			return new DefaultOAuth2AuthenticatedPrincipal(oldAuthorization.getPrincipalName(),
					Objects.requireNonNull(oldAuthorization.getAccessToken().getClaims()), AuthorityUtils.NO_AUTHORITIES); // 客户端模式无用户权限
		}

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(PigUserDetailsService.class);

		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values()
			.stream()
			.filter(service -> service.support(Objects.requireNonNull(oldAuthorization).getRegisteredClientId(),
					oldAuthorization.getAuthorizationGrantType().getValue()))
			.max(Comparator.comparingInt(Ordered::getOrder));

		UserDetails userDetails = null;
		try {
			Object principal = Objects.requireNonNull(oldAuthorization).getAttributes().get(Principal.class.getName());
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
			Object tokenPrincipal = usernamePasswordAuthenticationToken.getPrincipal();
			userDetails = optional.get().loadUserByUser((PigUser) tokenPrincipal);
		} catch (UsernameNotFoundException notFoundException) {
			log.error("***************** 用户不不存在 {}", notFoundException.getLocalizedMessage());
			throw notFoundException;
		} catch (Exception ex) {
			log.error("***************** 资源服务器 introspect Token error {}", ex.getLocalizedMessage());
		}

		// 注入客户端信息,方便上下文中获取
		PigUser pigUser = (PigUser) userDetails;
		Objects.requireNonNull(pigUser).getAttributes().put(SecurityConstants.CLIENT_ID, oldAuthorization.getRegisteredClientId());
		return pigUser;
	}

}
