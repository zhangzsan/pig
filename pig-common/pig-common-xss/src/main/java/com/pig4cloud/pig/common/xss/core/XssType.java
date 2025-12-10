package com.pig4cloud.pig.common.xss.core;

import com.pig4cloud.pig.common.xss.exception.FromXssException;

/**
 * xss 数据处理类型
 */
public enum XssType {

	/**
	 * 表单
	 */
	FORM() {
		@Override
		public RuntimeException getXssException(String input, String message) {
			return new FromXssException(input, message);
		}
	},

	/**
	 * body json
	 */
	JACKSON() {
		@Override
		public RuntimeException getXssException(String input, String message) {
			return new RuntimeException(message);
		}
	};

	/**
	 * 获取xss异常
	 */
	public abstract RuntimeException getXssException(String input, String message);

}
