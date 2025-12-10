package com.pig4cloud.pig.common.security.component;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;


/**
 * 资源服务器认证授权配置
 */
@EnableWebSecurity  //启用Web层安全配置,配置HTTP请求级别的安全控制
@EnableMethodSecurity  // 启用方法级别安全控制,提供细粒度的权限检查。
@RequiredArgsConstructor
public class PigResourceServerConfiguration {

    /**
	 * 资源认证异常处理入口点
	 */
	protected final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

	/**
	 * 允许所有URL的配置属性
	 */
	private final PermitAllUrlProperties permitAllUrl;

	/**
	 * PigBearerToken提取器
	 */
	private final PigBearerTokenExtractor pigBearerTokenExtractor;

	/**
	 * 自定义不透明令牌解析器
	 */
	private final OpaqueTokenIntrospector customOpaqueTokenIntrospector;

	/**
	 * 资源服务器安全配置  配置1：授权服务器自身的安全过滤器链
	 */
	@Bean
	SecurityFilterChain resourceServer(HttpSecurity http) throws Exception {
		PathPatternRequestMatcher[] permitMatchers = permitAllUrl.getUrls()
			.stream()
			.map(url -> PathPatternRequestMatcher.withDefaults().matcher(url))
			.toList()
			.toArray(new PathPatternRequestMatcher[] {});

		http.authorizeHttpRequests(req -> req.requestMatchers(permitMatchers)
			.permitAll()
			.anyRequest()
			.authenticated())
			.oauth2ResourceServer(oauth2 ->
                            oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))//用于验证令牌的有效性,不透明令牌需要向授权服务器发起请求来验证令牌。
						.authenticationEntryPoint(resourceAuthExceptionEntryPoint) //自定义认证入口点,用于处理认证失败的情况(比如令牌无效、过期等)
						.bearerTokenResolver(pigBearerTokenExtractor))//自定义承载令牌解析器,用于从请求中提取令牌(例如从请求头、参数等中提取)
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}

}
