package com.pig4cloud.pig.daemon.quartz.event;

import org.quartz.Trigger;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.util.TaskInvokUtil;

import lombok.RequiredArgsConstructor;

/**
 * 系统任务监听器：用于异步监听并处理定时任务事件
 */
@Service
@RequiredArgsConstructor
public class SysJobListener {

	private final TaskInvokUtil taskInvokUtil;

	@Async
	@Order
	@EventListener(SysJobEvent.class)
	public void comSysJob(SysJobEvent event) {
		SysJob sysJob = event.getSysJob();
		Trigger trigger = event.getTrigger();
		taskInvokUtil.invokMethod(sysJob, trigger);
	}

}
