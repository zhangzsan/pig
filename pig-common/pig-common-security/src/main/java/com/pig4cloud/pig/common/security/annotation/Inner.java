package com.pig4cloud.pig.common.security.annotation;

import java.lang.annotation.*;

/**
 * 内部注解：用于标记方法或类型是否需要AOP统一处理,内部的调用,不需要进行权限验证
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

	/**
	 * 是否AOP统一处理
	 */
	boolean value() default true;

	/**
	 * 需要特殊判空的字段(预留)
	 */
	String[] field() default {};

}
