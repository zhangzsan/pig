package com.pig4cloud.pig.common.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

/**
 * Pig Feign 客户端配置类
 */
public class PigFeignClientConfiguration {

	/**
	 * 注入oauth2 feign token增强,token获取处理器
	 */
	@Bean
	public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
		return new PigOAuthRequestInterceptor(tokenResolver);
	}

}
