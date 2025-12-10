package com.pig4cloud.pig.common.log.event;

import org.springframework.context.ApplicationEvent;
import com.pig4cloud.pig.admin.api.entity.SysLog;

/**
 * 系统日志事件类,使用spring中的事件监听机制
 */
public class SysLogEvent extends ApplicationEvent {

	/**
	 * 构造方法,根据源SysLog对象创建SysLogEvent,异步保存日志
	 */
	public SysLogEvent(SysLog source) {
		super(source);
	}

}
