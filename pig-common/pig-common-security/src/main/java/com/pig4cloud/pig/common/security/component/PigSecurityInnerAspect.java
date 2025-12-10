package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.text.CharSequenceUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.annotation.Inner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

/**
 * 服务间接口不鉴权处理切面
 *
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class PigSecurityInnerAspect implements Ordered {

	private final HttpServletRequest request;

    /**
	 * 环绕通知，用于检查内部调用权限
	 * point 切点对象
	 * inner 内部调用注解
	 */
	@Before("@within(inner) || @annotation(inner)")
	public void around(JoinPoint point, Inner inner) {
		// 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
		if (inner == null) {
			Class<?> clazz = point.getTarget().getClass();
			inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
		}
		String header = request.getHeader(SecurityConstants.FROM);
        assert inner != null;
        if (inner.value() && !CharSequenceUtil.equals(SecurityConstants.FROM_IN, header)) {
			log.warn("访问接口 {} 没有权限", point.getSignature().getName());
			throw new AccessDeniedException("Access is denied");
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}
