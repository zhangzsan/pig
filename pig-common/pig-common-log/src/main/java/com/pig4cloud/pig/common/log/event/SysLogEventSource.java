package com.pig4cloud.pig.common.log.event;

import com.pig4cloud.pig.admin.api.entity.SysLog;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志事件源类，继承自SysLog
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogEventSource extends SysLog {
	/**
	 * 参数重写成object
	 */
	private Object body;

}
