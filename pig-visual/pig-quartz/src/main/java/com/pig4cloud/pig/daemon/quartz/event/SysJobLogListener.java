package com.pig4cloud.pig.daemon.quartz.event;

import com.pig4cloud.pig.daemon.quartz.entity.SysJobLog;
import com.pig4cloud.pig.daemon.quartz.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 系统任务日志监听器：用于异步监听并处理定时任务日志事件
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobLogListener {

	private final SysJobLogService sysJobLogService;

	/**
	 * 异步保存系统任务日志
	 */
	@Async
	@Order
	@EventListener(SysJobLogEvent.class)
	public void saveSysJobLog(SysJobLogEvent event) {
		SysJobLog sysJobLog = event.getSysJobLog();
		sysJobLogService.save(sysJobLog);
		log.info("执行定时任务日志");
	}

}
