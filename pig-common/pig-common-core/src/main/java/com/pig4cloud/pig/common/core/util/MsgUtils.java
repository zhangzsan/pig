package com.pig4cloud.pig.common.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * i18n 工具类
 */
@UtilityClass
public class MsgUtils {

	/**
	 * 根据错误码获取中文错误信息
	 */
	public String getMessage(String code) {
		MessageSource messageSource = SpringContextHolder.getBean("messageSource");
		return messageSource.getMessage(code, null, Locale.CHINA);
	}

	/**
	 * 通过错误码和参数获取中文错误信息
	 * @param objects 格式化参数
	 */
	public String getMessage(String code, Object... objects) {
		MessageSource messageSource = SpringContextHolder.getBean("messageSource");
		return messageSource.getMessage(code, objects, Locale.CHINA);
	}

	/**
	 * 通过错误码和参数获取中文错误信息
	 */
	public String getSecurityMessage(String code, Object... objects) {
		MessageSource messageSource = SpringContextHolder.getBean("securityMessageSource");
		return messageSource.getMessage(code, objects, Locale.CHINA);
	}

}
