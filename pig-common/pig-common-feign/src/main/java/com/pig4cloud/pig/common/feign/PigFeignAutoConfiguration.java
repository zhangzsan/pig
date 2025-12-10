package com.pig4cloud.pig.common.feign;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.pig4cloud.pig.common.feign.core.PigFeignInnerRequestInterceptor;
import com.pig4cloud.pig.common.feign.core.PigFeignRequestCloseInterceptor;
import com.pig4cloud.pig.common.feign.sentinel.ext.PigSentinelFeign;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.PigFeignClientsRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * Sentinel Feign 自动配置类
 *
 * @author lengleng
 */
@Configuration(proxyBeanMethods = false)
@Import(PigFeignClientsRegistrar.class)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class PigFeignAutoConfiguration {

	/**
	 * 创建Feign.Builder实例，支持Sentinel功能
	 * @return Feign.Builder实例
	 * @ ConditionalOnMissingBean 当容器中不存在该类型bean时创建
	 * @ ConditionalOnProperty 当配置feign.sentinel.enabled为true时生效
	 * @ Scope 指定bean作用域为prototype
	 */
	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "feign.sentinel.enabled")
	public Feign.Builder feignSentinelBuilder() {
		return PigSentinelFeign.builder();
	}

	/**
	 * 创建并返回PigFeignRequestCloseInterceptor实例
	 * @return PigFeignRequestCloseInterceptor实例
	 */
	@Bean
	public PigFeignRequestCloseInterceptor pigFeignRequestCloseInterceptor() {
		return new PigFeignRequestCloseInterceptor();
	}

	/**
	 * 创建并返回PigFeignInnerRequestInterceptor实例
	 * @return PigFeignInnerRequestInterceptor 内部请求拦截器实例
	 */
	@Bean
	public PigFeignInnerRequestInterceptor pigFeignInnerRequestInterceptor() {
		return new PigFeignInnerRequestInterceptor();
	}

}
