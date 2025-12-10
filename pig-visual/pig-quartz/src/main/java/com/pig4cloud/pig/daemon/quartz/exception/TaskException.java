package com.pig4cloud.pig.daemon.quartz.exception;

import java.io.Serial;

/**
 * 定时任务异常类
 */
public class TaskException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	public TaskException() {
		super();
	}


	public TaskException(String msg) {
		super(msg);
	}

}
