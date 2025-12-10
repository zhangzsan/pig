package com.pig4cloud.pig.common.xss.exception;

import lombok.Getter;

import java.io.IOException;
import java.io.Serial;

/**
 * Jackson XSS 异常类，用于处理 JSON 序列化/反序列化过程中的 XSS 安全问题
 */
@Getter
public class JacksonXssException extends IOException implements XssException {

	private final String input;

	/**
	 * 构造JacksonXssException异常
	 */
	public JacksonXssException(String input, String message) {
		super(message);
		this.input = input;
	}

}
