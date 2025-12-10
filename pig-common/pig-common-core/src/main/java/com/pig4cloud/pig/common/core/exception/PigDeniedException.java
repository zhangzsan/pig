package com.pig4cloud.pig.common.core.exception;


/**
 * 授权拒绝异常类
 */
public class PigDeniedException extends RuntimeException {

	public PigDeniedException(String message) {
		super(message);
	}

	public PigDeniedException(Throwable cause) {
		super(cause);
	}

	public PigDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public PigDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
