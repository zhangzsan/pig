
package com.pig4cloud.pig.common.security.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于验证token
 */
public class PigBearerTokenExtractor implements BearerTokenResolver {

	private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

    private final PathMatcher pathMatcher = new AntPathMatcher();  //路径匹配器

    //不需要权限认证的url
	private final PermitAllUrlProperties urlProperties; // 白名单配置

	public PigBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
		this.urlProperties = urlProperties;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		boolean match = urlProperties.getUrls()
			.stream()
			.anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

		if (match) {
			return null; // 白名单路径,不进行令牌验证
		}

		String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
		String parameterToken = isParameterTokenSupportedForRequest(request)
				? resolveFromRequestParameters(request) : null;
		if (authorizationHeaderToken != null) {
			if (parameterToken != null) {
				BearerTokenError error = BearerTokenErrors
					.invalidRequest("Found multiple bearer tokens in the request");
				throw new OAuth2AuthenticationException(error);
			}
			return authorizationHeaderToken;
		}
		if (parameterToken != null && isParameterTokenEnabledForRequest(request)) {
			return parameterToken;
		}
		return null;
	}

    /**
     *   获取Token
     */
	private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;
        String authorization = request.getHeader(bearerTokenHeaderName);
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}
		Matcher matcher = authorizationPattern.matcher(authorization);
		if (!matcher.matches()) {
			BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
			throw new OAuth2AuthenticationException(error);
		}
		return matcher.group("token");
	}

    /**
     *  access_token只能有一个,当多于一个时报错
     */
	private static String resolveFromRequestParameters(HttpServletRequest request) {
		String[] values = request.getParameterValues("access_token");
		if (values == null || values.length == 0) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
		throw new OAuth2AuthenticationException(error);
	}

	private boolean isParameterTokenSupportedForRequest(HttpServletRequest request) {
		return (("POST".equals(request.getMethod()) && MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| "GET".equals(request.getMethod()));
	}

	private boolean isParameterTokenEnabledForRequest(HttpServletRequest request) {
        // 允许URL参数传递令牌
        return "GET".equals(request.getMethod());
	}

}
