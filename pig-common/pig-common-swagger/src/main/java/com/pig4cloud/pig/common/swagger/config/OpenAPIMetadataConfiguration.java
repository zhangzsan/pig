package com.pig4cloud.pig.common.swagger.config;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * OpenAPI 元数据配置类，用于配置并注册OpenAPI相关元数据
 */
public class OpenAPIMetadataConfiguration implements InitializingBean, ApplicationContextAware {

    @Setter
    private String path;
	/**
	 * 应用上下文
	 */
    @Setter
	private ApplicationContext applicationContext;

	/**
	 * 在属性设置完成后执行，将spring-doc路径信息注册到ServiceInstance的元数据中
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		String[] beanNamesForType = applicationContext.getBeanNamesForType(ServiceInstance.class);
		if (beanNamesForType.length == 0) {
			return;
		}
		ServiceInstance serviceInstance = applicationContext.getBean(ServiceInstance.class);
		serviceInstance.getMetadata().put("spring-doc", path);
	}

}
