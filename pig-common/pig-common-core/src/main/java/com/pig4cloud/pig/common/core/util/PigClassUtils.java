package com.pig4cloud.pig.common.core.util;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 类工具类
 */
public class PigClassUtils extends ClassUtils {

	private static final ParameterNameDiscoverer PARAMETERNAMEDISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * 获取方法参数信息
	 * @param constructor 构造器
	 * @param parameterIndex 参数序号
	 */
	public MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
		MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
		methodParameter.initParameterNameDiscovery(PARAMETERNAMEDISCOVERER);
		return methodParameter;
	}

	/**
	 * 获取方法参数信息
	 * @param method 方法
	 * @param parameterIndex 参数序号
	 */
	public MethodParameter getMethodParameter(Method method, int parameterIndex) {
		MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
		methodParameter.initParameterNameDiscovery(PARAMETERNAMEDISCOVERER);
		return methodParameter;
	}

	/**
	 * 获取Annotation
	 * @param method Method
	 * @param annotationType 注解类
	 */
	public <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		Class<?> targetClass = method.getDeclaringClass();
		Method specificMethod = PigClassUtils.getMostSpecificMethod(method, targetClass);
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
		A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
		if (null != annotation) {
			return annotation;
		}
		return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
	}

	/**
	 * 获取Annotation
	 * @param handlerMethod HandlerMethod
	 * @param annotationType 注解类
	 * @param <A> 泛型标记
	 * @return {Annotation}
	 */
	public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
		// 先找方法，再找方法上的类
		A annotation = handlerMethod.getMethodAnnotation(annotationType);
		if (null != annotation) {
			return annotation;
		}
		// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
		Class<?> beanType = handlerMethod.getBeanType();
		return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
	}

}
