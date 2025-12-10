
package com.pig4cloud.pig.daemon.quartz.config;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;

import lombok.SneakyThrows;

/**
 * 动态任务工厂：用于执行动态任务调度
 */
@DisallowConcurrentExecution
public class PigQuartzFactory implements Job {

	/**
	 * 定时任务调用工厂
	 */
	@Autowired
	private PigQuartzInvokeFactory pigxQuartzInvokeFactory;

	/**
	 * 执行定时任务
	 * @param jobExecutionContext 任务执行上下文
	 * @throws Exception 执行过程中可能抛出的异常
	 */
	@Override
	@SneakyThrows
	public void execute(JobExecutionContext jobExecutionContext) {
		SysJob sysJob = (SysJob) jobExecutionContext.getMergedJobDataMap()
			.get(PigQuartzEnum.SCHEDULE_JOB_KEY.getType());
		pigxQuartzInvokeFactory.init(sysJob, jobExecutionContext.getTrigger());
	}

}
