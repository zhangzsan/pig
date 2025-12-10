package com.pig4cloud.pig.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 启用Pig Feign客户端注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnablePigFeignClients {

	/**
	 * 属性的别名。允许更简洁的注解声明
	 */
	String[] value() default {};

	/**
	 * 扫描注解组件的基础包路径,对于基于字符串的包名，可使用{@link ()}作为类型安全的替代方案
	 */
	@AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
	String[] basePackages() default { "com.pig4cloud.pig" };

}
