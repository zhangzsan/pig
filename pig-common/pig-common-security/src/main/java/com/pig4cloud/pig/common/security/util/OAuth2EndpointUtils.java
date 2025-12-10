package com.pig4cloud.pig.common.security.util;

import cn.hutool.core.map.MapUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * OAuth2 端点工具类,提供OAuth2相关端点操作的实用方法
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2EndpointUtils {

    /**
     * OAuth2 错误信息 URI
     */
    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * 从HttpServletRequest中获取参数并转换为MultiValueMap
     * request HttpServletRequest对象
     * 包含所有参数的MultiValueMap，key为参数名，value为参数值列表
     */
    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    /**
     * 抛出OAuth2认证异常
     * errorCode     错误码
     * parameterName 参数名称
     */
    public static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throw new OAuth2AuthenticationException(error);
    }

    /**
     * 发送OAuth2访问令牌响应
     */
    public static OAuth2AccessTokenResponse sendAccessTokenResponse(OAuth2Authorization authentication, Map<String, Object> claims) {
        // 获取访问令牌
        OAuth2AccessToken accessToken = authentication.getAccessToken().getToken();
        //获取刷新令牌
        OAuth2Authorization.Token<OAuth2RefreshToken> oAuth2RefreshToken = authentication.getRefreshToken();
        OAuth2RefreshToken refreshToken = null;
        if (oAuth2RefreshToken != null) {
            refreshToken = oAuth2RefreshToken.getToken();
        }
        //构建访问令牌响应
        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }

        if (MapUtil.isNotEmpty(claims)) {
            builder.additionalParameters(claims);
        }
        return builder.build();
    }

}
