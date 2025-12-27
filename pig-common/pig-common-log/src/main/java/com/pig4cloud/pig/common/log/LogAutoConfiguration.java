package com.pig4cloud.pig.common.log;

import com.pig4cloud.pig.admin.api.feign.RemoteLogService;
import com.pig4cloud.pig.common.log.aspect.SysLogAspect;
import com.pig4cloud.pig.common.log.config.PigLogProperties;
import com.pig4cloud.pig.common.log.event.SysLogListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志自动配置类，用于配置系统日志相关功能
 */
@EnableAsync       // 启用Spring的异步方法执行功能
@Configuration(proxyBeanMethods = false)  // 配置类,不代理Bean方法
@EnableConfigurationProperties(PigLogProperties.class)  // 启用配置属性绑定
@ConditionalOnProperty(value = "security.log.enabled", matchIfMissing = true) // 条件配置,表示如果配置文件中没有这个属性,默认启用日志功能
public class LogAutoConfiguration {

	/**
	 * 创建并返回SysLogListener的Bean实例
	 */
	@Bean
	public SysLogListener sysLogListener(PigLogProperties logProperties, RemoteLogService remoteLogService) {
		return new SysLogListener(remoteLogService, logProperties);
	}

	/**
	 * 创建并返回SysLogAspect的Bean实例,用于记录日志
	 */
	@Bean
	public SysLogAspect sysLogAspect() {
		return new SysLogAspect();
	}

}
