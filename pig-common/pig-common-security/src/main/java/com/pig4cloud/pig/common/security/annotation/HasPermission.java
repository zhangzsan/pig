package com.pig4cloud.pig.common.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解：用于方法级别的权限控制
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@pms.hasPermission('{value}'.split(','))")  //@PreAuthorize在方法执行前进行权限检查,@pms.hasPermission()
// - 调用名为pms的Bean的hasPermission方'{value}'.split(',')
// - 将权限字符串按逗号分割成数组
public @interface HasPermission {

	/**
	 * 权限字符串
	 */
	String[] value();
}
