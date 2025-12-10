package com.pig4cloud.pig.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 路由限流配置类
 */
@Configuration(proxyBeanMethods = false)
public class RateLimiterConfiguration {

	/**
	 * 创建基于远程地址的KeyResolver实例,基于ip地址限制
	 */
	@Bean
	public KeyResolver remoteAddrKeyResolver() {
		return exchange -> Mono
			.just(Objects.requireNonNull(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()))
				.getAddress()
				.getHostAddress());
	}

}
