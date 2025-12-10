package com.pig4cloud.pig.daemon.quartz.event;

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Trigger;

/**
 * 系统任务事件类，用于封装定时任务及其触发器
 */
@Getter
@AllArgsConstructor
public class SysJobEvent {

	private final SysJob sysJob;

	private final Trigger trigger;

}
