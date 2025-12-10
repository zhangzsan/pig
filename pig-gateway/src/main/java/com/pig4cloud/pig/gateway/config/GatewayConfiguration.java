package com.pig4cloud.pig.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.gateway.filter.PigRequestGlobalFilter;
import com.pig4cloud.pig.gateway.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置类
 * 1. 配置类中不存在@Bean方法之间的相互调用,或者即使有相互调用,也不需要保证单例。追求极致的启动速度,减少代理开销
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

	/**
	 * 创建PigRequest全局过滤器
	 */
	@Bean
	public PigRequestGlobalFilter pigRequestGlobalFilter() {
		return new PigRequestGlobalFilter();
	}

	/**
	 * 创建全局异常处理程序
	 */
	@Bean
	public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
		return new GlobalExceptionHandler(objectMapper);
	}

}
