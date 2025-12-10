package com.pig4cloud.pig.common.security.component;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 资源服务器自动配置类
 */
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class PigResourceServerAutoConfiguration {

    /**
	 * 鉴权具体的实现逻辑
	 */
	@Bean("pms")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * 请求令牌的抽取逻辑
	 * urlProperties 对外暴露的接口列表
	 */
	@Bean
	public PigBearerTokenExtractor pigBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
		return new PigBearerTokenExtractor(urlProperties);
	}

	/**
	 * 资源服务器异常处理
	 * objectMapper jackson 输出对象
	 * securityMessageSource 自定义国际化处理器
	 */
	@Bean
	public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper,
			MessageSource securityMessageSource) {
		return new ResourceAuthExceptionEntryPoint(objectMapper, securityMessageSource);
	}

	/**
	 * 资源服务器toke内省处理器
	 */
	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
		return new PigCustomOpaqueTokenIntrospector(authorizationService);
	}

	/**
	 * 支持自定义权限表达式
	 */
	@Bean
	AnnotationTemplateExpressionDefaults prePostTemplateDefaults() {
		return new AnnotationTemplateExpressionDefaults();
	}

}
