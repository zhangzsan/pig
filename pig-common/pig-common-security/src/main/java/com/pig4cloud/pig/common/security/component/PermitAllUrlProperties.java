package com.pig4cloud.pig.common.security.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.pig4cloud.pig.common.security.annotation.Inner;

import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;

/**
 * 资源服务器对外直接暴露URL配置类,用于配置不需要认证即可访问的URL路径，支持路径变量替换
 */
@Data
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlProperties implements InitializingBean {

    //替换被{}中的内容
	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

	private static final String[] DEFAULT_IGNORE_URLS = new String[] {
            "/actuator/**",
            "/error",
            "/v3/api-docs" };

	private List<String> urls = new ArrayList<>();

    /**
	 * 初始化方法,在属性设置完成后执行 收集带有@Inner注解的Controller方法路径，并将路径中的变量替换为*
	 */
	@Override
	public void afterPropertiesSet() {
		urls.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
		RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
			Optional.ofNullable(method)
				.ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
					.getPatternValues()
					.forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));

			Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
			Optional.ofNullable(controller)
				.ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
					.getPatternValues()
					.forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
		});
	}

}
