package com.pig4cloud.pig.daemon.quartz.config;

import org.quartz.JobKey;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.Assert;

/**
 * 自动装配能力的Bean任务工厂,继承自SpringBeanJobFactory，用于创建并自动装配Job实例
 */
class AutowireCapableBeanJobFactory extends SpringBeanJobFactory {

	private final AutowireCapableBeanFactory beanFactory;

	AutowireCapableBeanJobFactory(AutowireCapableBeanFactory beanFactory) {
		Assert.notNull(beanFactory, "Bean factory must not be null");
		this.beanFactory = beanFactory;
	}

	/**
	 * 创建并初始化Job实例
	 * @param bundle 触发器触发包，包含Job相关信息
	 * @return 初始化后的Job实例
	 * @throws Exception 创建或初始化过程中可能抛出的异常
	 */
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		this.beanFactory.autowireBean(jobInstance);

		// 此处必须注入 beanName 不然sentinel 报错
		JobKey jobKey = bundle.getTrigger().getJobKey();
		String beanName = jobKey + jobKey.getName();
		this.beanFactory.initializeBean(jobInstance, beanName);
		return jobInstance;
	}

}
