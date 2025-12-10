package com.pig4cloud.pig.daemon.quartz.event;

import com.pig4cloud.pig.daemon.quartz.entity.SysJobLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务日志多线程事件
 */
@Getter
@AllArgsConstructor
public class SysJobLogEvent {

	private final SysJobLog sysJobLog;

}
