package com.pig4cloud.pig.auth.support.base;

import com.pig4cloud.pig.common.security.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OAuth2资源所有者基础认证转换器抽象类,接口到请求信息.并将结果解析为OAuth2资源所有者密码认证令牌
 */
public abstract class OAuth2ResourceOwnerBaseAuthenticationConverter<T extends OAuth2ResourceOwnerBaseAuthenticationToken> implements AuthenticationConverter {

	/**
	 * 是否支持此convert
	 */
	public abstract boolean support(String grantType);

	/**
	 * 校验参数
	 */
	public void checkParams(HttpServletRequest request) {}

	/**
	 * 构建具体类型的token
	 * clientPrincipal 客户端认证信息
	 * requestedScopes 请求的作用域集合
	 * additionalParameters 附加参数映射
	 */
	public abstract T buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

	/**
	 * 将HttpServletRequest转换为Authentication对象
	 */
	@Override
	public Authentication convert(HttpServletRequest request) {
		// grant_type(REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!support(grantType)) {
			return null;
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // scope 参数, 多个作用域用空格隔,可无但是不能多于一个
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
            // 作用域集合,将scope按照空格分隔符进行分割
			requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		// 校验个性化参数
		checkParams(request);

		// 获取当前已经认证的客户端信息
		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		if (clientPrincipal == null) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 扩展信息
		Map<String, Object> additionalParameters = parameters.entrySet()
			.stream()
			.filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
					&& !e.getKey().equals(OAuth2ParameterNames.SCOPE))
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		// 创建token
		return buildToken(clientPrincipal, requestedScopes, additionalParameters);

	}

}
