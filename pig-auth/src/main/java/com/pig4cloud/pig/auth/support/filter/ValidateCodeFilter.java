package com.pig4cloud.pig.auth.support.filter;

import java.io.IOException;
import java.util.Optional;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import com.pig4cloud.pig.common.core.util.PigWebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 验证码过滤器：用于处理登录请求中的验证码校验,OncePerRequestFilter保证在一个http中只处理一次
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final AuthSecurityConfigProperties authSecurityConfigProperties;

    /**
     * 过滤器内部处理逻辑,用于验证码校验
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestUrl = request.getServletPath();

        // 不是登录URL 请求直接跳过
        if (!SecurityConstants.OAUTH_TOKEN_URL.equals(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("************************验证 **************************");
        // 如果登录URL 但是刷新token的请求,直接向下执行
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (CharSequenceUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 如果是密码模式 && 客户端不需要校验验证码
        boolean isIgnoreClient = authSecurityConfigProperties.getIgnoreClients().contains(PigWebUtils.getClientId());
        if (CharSequenceUtil.equalsAnyIgnoreCase(grantType, SecurityConstants.PASSWORD, SecurityConstants.CLIENT_CREDENTIALS, SecurityConstants.AUTHORIZATION_CODE) && isIgnoreClient) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验验证码 1.客户端开启验证码 2.短信模式
        try {
            checkCode();
            filterChain.doFilter(request, response);
        } catch (ValidateCodeException validateCodeException) {
            throw new OAuth2AuthenticationException(validateCodeException.getMessage());
        }
    }

    /**
     * 检验登录验证码是否一致
     */
    private void checkCode() throws ValidateCodeException {
        Optional<HttpServletRequest> request = PigWebUtils.getRequest();
        if (request.isEmpty()) {
            return;
        }

        HttpServletRequest httpServletRequest = request.get();
        String code = httpServletRequest.getParameter("code");

        if (CharSequenceUtil.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        String randomStr = request.get().getParameter("randomStr");

        String mobile = request.get().getParameter("mobile");
        //当有手机号时,替换随机字符串
        if (CharSequenceUtil.isNotBlank(mobile)) {
            randomStr = mobile;
        }

        String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
        if (!RedisUtils.hasKey(key)) {
            throw new ValidateCodeException("验证码不合法");
        }

        String saveCode = RedisUtils.get(key);

        if (CharSequenceUtil.isBlank(saveCode)) {
            RedisUtils.delete(key);
            throw new ValidateCodeException("验证码不合法");
        }

        if (!CharSequenceUtil.equals(saveCode, code)) {
            RedisUtils.delete(key);
            throw new ValidateCodeException("验证码不合法");
        }
    }

}
