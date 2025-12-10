package com.pig4cloud.pig.monitor.converter;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;
import org.wildfly.common.annotation.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

/**
 * Nacos 2.x 服务注册转换器,用于处理服务实例元数据转换
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

	/**
	 * 获取服务实例的元数据
	 */
	@Override
    @NotNull
	protected Map<String, String> getMetadata(ServiceInstance instance) {
		return (instance.getMetadata() != null) ? instance.getMetadata()
			.entrySet()
			.stream()
			.filter(e -> e.getKey() != null && e.getValue() != null)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : emptyMap();
	}

}
