package com.pig4cloud.pig.common.core.util;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Spring应用上下文工具类
 */
@Service
@Lazy(false)
@Slf4j
@SuppressWarnings("all")
public class SpringContextHolder implements ApplicationContextAware, EnvironmentAware, DisposableBean {

	@Getter
    private static ApplicationContext applicationContext = null;

    @Getter
    private static Environment environment = null;

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public void setEnvironment(Environment environment) {
        SpringContextHolder.environment = environment;
    }

    /**
	 * 从静态变量applicationContext中取得Bean, 根据名称获取类型自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (log.isDebugEnabled()) {
            log.debug("清除SpringContextHolder中的ApplicationContext: {}" ,  applicationContext);
		}
		applicationContext = null;
	}

	/**
	 * 发布事件
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (applicationContext == null) {
			return;
		}
		applicationContext.publishEvent(event);
	}

	/**
	 * 是否是微服务
	 * @return boolean
	 */
	public static boolean isMicro() {
		return environment.getProperty("spring.cloud.nacos.discovery.enabled", Boolean.class, true);
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	@SneakyThrows
	public void destroy() {
		clearHolder();
	}

}
