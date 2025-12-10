package com.pig4cloud.pig.common.xss;

import com.pig4cloud.pig.common.xss.config.PigXssProperties;
import com.pig4cloud.pig.common.xss.core.DefaultXssCleaner;
import com.pig4cloud.pig.common.xss.core.FormXssClean;
import com.pig4cloud.pig.common.xss.core.JacksonXssClean;
import com.pig4cloud.pig.common.xss.core.XssCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Jackson XSS 自动配置类
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(PigXssProperties.class)
@ConditionalOnProperty(prefix = PigXssProperties.PREFIX, name = "enabled",
		havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PigXssAutoConfiguration implements WebMvcConfigurer {

	private final PigXssProperties xssProperties;

	/**
	 * 创建XSS清理器Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public XssCleaner xssCleaner(PigXssProperties properties) {
		return new DefaultXssCleaner(properties);
	}

	/**
	 * 创建FormXssClean实例
	 * @param properties PigXss配置属性
	 * @param xssCleaner XSS清理器
	 */
	@Bean
	public FormXssClean formXssClean(PigXssProperties properties,
			XssCleaner xssCleaner) {
		return new FormXssClean(properties, xssCleaner);
	}

	/**
	 * 创建Jackson2ObjectMapperBuilderCustomizer Bean，用于XSS防护
	 * @param properties XSS配置属性
	 * @param xssCleaner XSS清理器
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer(
			PigXssProperties properties, XssCleaner xssCleaner) {
		return builder -> builder.deserializerByType(String.class, new JacksonXssClean(properties, xssCleaner));
	}

	/**
	 * 添加XSS拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> patterns = xssProperties.getPathPatterns();
		if (patterns.isEmpty()) {
			patterns.add("/**");
		}
		com.pig4cloud.pig.common.xss.core.XssCleanInterceptor interceptor = new com.pig4cloud.pig.common.xss.core.XssCleanInterceptor(
				xssProperties);
		registry.addInterceptor(interceptor)
			.addPathPatterns(patterns)
			.excludePathPatterns(xssProperties.getPathExcludePatterns())
			.order(Ordered.LOWEST_PRECEDENCE);
	}

}
