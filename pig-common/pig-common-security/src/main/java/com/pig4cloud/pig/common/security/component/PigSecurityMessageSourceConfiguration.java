package com.pig4cloud.pig.common.security.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * 创建并配置安全相关的消息源
 */
@ConditionalOnWebApplication(type = SERVLET)
public class PigSecurityMessageSourceConfiguration implements WebMvcConfigurer {

	/**
	 * 创建并配置安全相关的消息源
     * 默认语言为中文
	 */
	@Bean
	public MessageSource securityMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("classpath:i18n/errors/messages");
		messageSource.setDefaultLocale(Locale.CHINA);
		return messageSource;
	}

}
