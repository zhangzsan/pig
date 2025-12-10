package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.common.core.util.RedisUtils;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现的OAuth2授权服务类
 */
@NoArgsConstructor
public class PigRedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final Long TIMEOUT = 10L;

    private static final String AUTHORIZATION = "token";

    private static final String STATE = "state";


    /**
     * 保存OAuth2授权信息
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        //存储state
        if (isState(authorization)) {
            String token = authorization.getAttribute(STATE);
            RedisUtils.set(buildKey(OAuth2ParameterNames.STATE, token), authorization, TIMEOUT, TimeUnit.MINUTES);
        }

        //存储code
        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            if (authorizationCodeToken != null) {
                assert authorizationCodeToken.getIssuedAt() != null;
                long between = ChronoUnit.MINUTES.between(authorizationCodeToken.getIssuedAt(),
                        authorizationCodeToken.getExpiresAt());
                RedisUtils.set(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()), authorization,
                        between, TimeUnit.MINUTES);
            }
        }

        //存储refreshToken
        if (isRefreshToken(authorization) && authorization.getRefreshToken() != null) {
            OAuth2Authorization.Token<OAuth2RefreshToken> oAuth2AuthorizationRefreshToken = authorization.getRefreshToken();
            OAuth2RefreshToken refreshToken = oAuth2AuthorizationRefreshToken.getToken();
            assert refreshToken.getIssuedAt() != null;
            long between = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            RedisUtils.set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()), authorization,
                    between, TimeUnit.SECONDS);
        }


        // 存储accessToken
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            assert accessToken.getIssuedAt() != null;
            long between = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            RedisUtils.set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()), authorization,
                    between, TimeUnit.SECONDS);
        }
    }

    /**
     * 移除OAuth2授权信息
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        List<String> keys = new ArrayList<>();
        if (isState(authorization)) {
            String token = authorization.getAttribute(STATE);
            keys.add(buildKey(OAuth2ParameterNames.STATE, token));
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            keys.add(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
        }

        if (isRefreshToken(authorization) && authorization.getRefreshToken() != null) {
            OAuth2Authorization.Token<OAuth2RefreshToken> oAuthRefreshToken = authorization.getRefreshToken();
            OAuth2RefreshToken refreshToken = oAuthRefreshToken.getToken();
            keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
        }
        RedisUtils.delete(keys.toArray(String[]::new));
    }

    /**
     * 根据ID查询OAuth2授权信息
     *
     */
    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据token和token类型查询OAuth2授权信息
     *
     */
    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");
        return RedisUtils.get(buildKey(tokenType.getValue(), token));
    }

    /**
     * 构建key
     */
    private String buildKey(String type, String id) {
        return String.format("%s::%s::%s", AUTHORIZATION, type, id);
    }

    /**
     * 检查授权对象是否包含state属性
     */
    private static boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute(STATE));
    }

    /**
     * 检查授权对象是否包含授权码
     *
     */
    private static boolean isCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        return Objects.nonNull(authorizationCode);
    }

    /**
     * 判断授权是否包含刷新令牌
     */
    private static boolean isRefreshToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getRefreshToken());
    }

    /**
     * 判断授权对象是否包含访问令牌
     */
    private static boolean isAccessToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAccessToken());
    }
}
