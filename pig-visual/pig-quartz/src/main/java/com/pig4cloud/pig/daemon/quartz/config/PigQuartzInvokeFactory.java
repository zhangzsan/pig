
package com.pig4cloud.pig.daemon.quartz.config;

import org.aspectj.lang.annotation.Aspect;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.event.SysJobEvent;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 定时任务调用工厂类 用于初始化并发布定时任务事件
 */
@Aspect
@Service
@AllArgsConstructor
public class PigQuartzInvokeFactory {

	private final ApplicationEventPublisher publisher;

	/**
	 * 初始化并发布定时任务事件
	 * @param sysJob 系统任务对象
	 * @param trigger 任务触发器
	 */
	@SneakyThrows
	void init(SysJob sysJob, Trigger trigger) {
		publisher.publishEvent(new SysJobEvent(sysJob, trigger));
	}

}
