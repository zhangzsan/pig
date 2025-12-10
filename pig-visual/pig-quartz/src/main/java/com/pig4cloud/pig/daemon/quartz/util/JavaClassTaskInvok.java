package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 基于Java反射实现的定时任务调用类
 */
@Component("javaClassTaskInvok")
@Slf4j
public class JavaClassTaskInvok implements ITaskInvok {

	/**
	 * 调用定时任务方法
	 * @param sysJob 定时任务信息
	 * @throws TaskException 执行任务过程中出现异常时抛出
	 */
	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		Object obj;
		Class clazz;
		Method method;
		Object returnValue;
		try {
			if (CharSequenceUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
				clazz = Class.forName(sysJob.getClassName());
				obj = clazz.getDeclaredConstructor().newInstance();
				method = clazz.getDeclaredMethod(sysJob.getMethodName(), String.class);
				returnValue = method.invoke(obj, sysJob.getMethodParamsValue());
			}
			else {
				clazz = Class.forName(sysJob.getClassName());
				obj = clazz.getDeclaredConstructor().newInstance();
				method = clazz.getDeclaredMethod(sysJob.getMethodName());
				returnValue = method.invoke(obj);
			}
			if (CharSequenceUtil.isEmpty(returnValue.toString())
					|| PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())) {
				log.error("定时任务javaClassTaskInvok异常,执行任务：{}", sysJob.getClassName());
				throw new TaskException("定时任务javaClassTaskInvok业务执行失败,任务：" + sysJob.getClassName());
			}
		} catch (ClassNotFoundException e) {
			log.error("定时任务java反射类没有找到,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类没有找到,执行任务：" + sysJob.getClassName());
		} catch (IllegalAccessException | InstantiationException e) {
			log.error("定时任务java反射类异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类异常,执行任务：" + sysJob.getClassName());
		} catch (NoSuchMethodException e) {
			log.error("定时任务java反射执行方法名异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射执行方法名异常,执行任务：" + sysJob.getClassName());
		} catch (InvocationTargetException e) {
			log.error("定时任务java反射执行异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射执行异常,执行任务：" + sysJob.getClassName());
		}

	}

}
