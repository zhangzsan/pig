package com.pig4cloud.pig.common.log.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.log.config.PigLogProperties;
import com.pig4cloud.pig.common.log.event.SysLogEventSource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * 系统日志工具类,封装请求日志类操作
 */
@UtilityClass
public class SysLogUtils {

	/**
	 * 获取系统日志事件源,系统日志事件源对象
	 */
	public SysLogEventSource getSysLog() {
        //获取当前请求
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		SysLogEventSource sysLog = new SysLogEventSource();
		sysLog.setLogType(LogTypeEnum.NORMAL.getType());
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setMethod(request.getMethod());
		sysLog.setRemoteAddr(JakartaServletUtil.getClientIP(request));
		sysLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		sysLog.setCreateBy(getUsername());
		sysLog.setServiceId(SpringUtil.getProperty("spring.application.name"));

        //封装请求日志的参数,不记录敏感信息
		PigLogProperties logProperties = SpringContextHolder.getBean(PigLogProperties.class);
		Map<String, String[]> paramsMap = MapUtil.removeAny(new HashMap<>(request.getParameterMap()),
				ArrayUtil.toArray(logProperties.getExcludeFields(), String.class));
		sysLog.setParams(HttpUtil.toParams(paramsMap));
		return sysLog;
	}

	/**
	 * 获取用户名称
	 */
	private String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getName();
	}

	/**
	 * 获取表达式,将表达式中的值替换其中的value值
	 */
	public <T> T getValue(EvaluationContext context, String key, Class<T> clazz) {
		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
		Expression expression = spelExpressionParser.parseExpression(key);
		return expression.getValue(context, clazz);
	}

	/**
	 * 获取参数容器
	 */
	public EvaluationContext getContext(Object[] arguments, Method signatureMethod) {
		String[] parameterNames = new StandardReflectionParameterNameDiscoverer().getParameterNames(signatureMethod);
		EvaluationContext context = new StandardEvaluationContext();
		if (parameterNames == null) {
			return context;
		}
		for (int i = 0; i < arguments.length; i++) {
			context.setVariable(parameterNames[i], arguments[i]);
		}
		return context;
	}

}
