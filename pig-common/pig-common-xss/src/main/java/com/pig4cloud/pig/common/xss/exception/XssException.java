package com.pig4cloud.pig.common.xss.exception;

/**
 * xss异常,校验模式抛出
 */
public interface XssException {

	/**
	 * 输入的数据
	 */
	String getInput();

	/**
	 * 获取异常的消息
	 */
	String getMessage();

}
