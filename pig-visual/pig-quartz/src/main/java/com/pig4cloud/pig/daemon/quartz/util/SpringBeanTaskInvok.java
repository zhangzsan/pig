package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 基于Spring Bean的定时任务反射执行器
 */
@Component("springBeanTaskInvok")
@Slf4j
public class SpringBeanTaskInvok implements ITaskInvok {

	/**
	 * 调用定时任务方法
	 * @param sysJob 定时任务信息
	 * @throws TaskException 当任务执行失败或反射调用异常时抛出
	 */
	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		Object target;
		Method method;
		Object returnValue;
		// 通过Spring上下文去找 也有可能找不到
		target = SpringContextHolder.getBean(sysJob.getClassName());
		try {
			if (StrUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
				method = target.getClass().getDeclaredMethod(sysJob.getMethodName(), String.class);
				ReflectionUtils.makeAccessible(method);
				returnValue = method.invoke(target, sysJob.getMethodParamsValue());
			}
			else {
				method = target.getClass().getDeclaredMethod(sysJob.getMethodName());
				ReflectionUtils.makeAccessible(method);
				returnValue = method.invoke(target);
			}
			if (StrUtil.isEmpty(returnValue.toString())
					|| PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())) {
				log.error("定时任务springBeanTaskInvok异常,执行任务：{}", sysJob.getClassName());
				throw new TaskException("定时任务springBeanTaskInvok业务执行失败,任务：" + sysJob.getClassName());
			}
		}
		catch (NoSuchMethodException e) {
			log.error("定时任务spring bean反射异常方法未找到,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务spring bean反射异常方法未找到,执行任务：" + sysJob.getClassName());
		}
		catch (IllegalAccessException e) {
			log.error("定时任务spring bean反射异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务spring bean反射异常,执行任务：" + sysJob.getClassName());
		}
		catch (InvocationTargetException e) {
			log.error("定时任务spring bean反射执行异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务spring bean反射执行异常,执行任务：" + sysJob.getClassName());
		}
	}

}
