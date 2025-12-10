package com.pig4cloud.pig.common.xss.exception;

import java.io.Serial;

import lombok.Getter;

/**
 * XSS 表单异常类,用于处理表单相关的 XSS 异常情况
 */
@Getter
public class FromXssException extends IllegalStateException implements XssException {

	private final String input;

	/**
	 * 构造FromXssException异常
	 */
	public FromXssException(String input, String message) {
		super(message);
		this.input = input;
	}

}
