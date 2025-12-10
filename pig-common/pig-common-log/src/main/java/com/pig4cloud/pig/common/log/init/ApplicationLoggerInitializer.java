package com.pig4cloud.pig.common.log.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 应用日志初始化类：通过环境变量注入logging.file 自动维护Spring Boot Admin Logger Viewer
 * 其他扩展点(如EnvironmentPostProcessor、ApplicationListener、FailureAnalyzer等）仍然需要放在spring.factories中。
 */
public class ApplicationLoggerInitializer implements EnvironmentPostProcessor, Ordered {

	/**
	 * 后处理环境配置,设置日志路径和相关系统属性,禁用部分功能防止日志文件过大
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String appName = environment.getProperty("spring.application.name");
		String logBase = environment.getProperty("LOGGING_PATH", "logs");

		// spring boot admin 直接加载日志
		System.setProperty("logging.file.name", String.format("%s/%s/debug.log", logBase, appName));

		// 避免各种依赖的地方组件造成 BeanPostProcessorChecker 警告
		System.setProperty("logging.level.org.springframework.context.support.PostProcessorRegistrationDelegate",
				"ERROR");

		// 避免sentinel 1.8.4+ 心跳日志过大
		System.setProperty("csp.sentinel.log.level", "OFF");

		// 避免sentinel健康检查server
		System.setProperty("management.health.sentinel.enabled", "false");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
