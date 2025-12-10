package com.pig4cloud.pig.common.xss.core;

import java.lang.annotation.*;

/**
 * 忽略 xss
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssCleanIgnore {

	String[] value() default {};

}
