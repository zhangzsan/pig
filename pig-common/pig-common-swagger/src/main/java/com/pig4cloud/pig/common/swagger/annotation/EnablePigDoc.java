package com.pig4cloud.pig.common.swagger.annotation;

import com.pig4cloud.pig.common.core.factory.YamlPropertySourceFactory;
import com.pig4cloud.pig.common.swagger.config.OpenAPIDefinitionImportSelector;
import com.pig4cloud.pig.common.swagger.support.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.*;

/**
 * 启用Pig框架的Spring文档支持
 * PropertySource默认读取properties文件，这里改为yaml文件,并指定factory类
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
@Import(OpenAPIDefinitionImportSelector.class)
@PropertySource(value = "classpath:openapi-config.yaml", factory = YamlPropertySourceFactory.class)
public @interface EnablePigDoc {

	/**
	 * 网关路由前缀
	 */
	String value();

	/**
	 * 是否是微服务架构
	 */
	boolean isMicro() default true;

}
