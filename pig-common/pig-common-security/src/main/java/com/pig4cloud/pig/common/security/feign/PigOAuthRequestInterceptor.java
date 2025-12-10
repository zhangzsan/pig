package com.pig4cloud.pig.common.security.feign;

import java.util.Collection;
import java.util.Optional;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.PigWebUtils;

import cn.hutool.core.collection.CollUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * oauth2 feign token传递,重新OAuth2FeignRequestInterceptor,官方实现部分常见不适用
 */
@RequiredArgsConstructor
@Slf4j
public class PigOAuthRequestInterceptor implements RequestInterceptor {

	/**
	 * 用于解析Bearer令牌的解析器
	 */
	private final BearerTokenResolver tokenResolver;

	/**
	 * 1. 如果使用非web请求header区别
	 * 2. 根据authentication还原请求token
	 */
	@Override
	public void apply(RequestTemplate template) {
        log.info("********************** PigOAuthRequestInterceptor apply ********************");
		Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
		//带from请求直接跳过
		if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
			return;
		}

        // 非web 请求直接跳过
        Optional<HttpServletRequest> requestOptional = PigWebUtils.getRequest();
        if (requestOptional.isEmpty()) {
            return;
        }
        HttpServletRequest request = requestOptional.get();
        // 避免请求参数的query token无法传递
        String token = tokenResolver.resolve(request);
        if (CharSequenceUtil.isBlank(token)) {
            return;
        }
		template.header(HttpHeaders.AUTHORIZATION,
				String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));

	}

}
