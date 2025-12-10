package com.pig4cloud.pig.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置
 */
@AutoConfiguration
public class RestTemplateConfiguration {

    /**
     * 创建支持负载均衡的REST客户端,Nacos服务发现启用时生效
     */
	@Bean
	@LoadBalanced
	@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

    /**
     *  创建支持负载均衡的REST客户端
     */
	@Bean
	@LoadBalanced
	@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

}
