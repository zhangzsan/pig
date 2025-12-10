package com.pig4cloud.pig.common.log.aspect;

import cn.hutool.core.text.CharSequenceUtil;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.log.event.SysLogEvent;
import com.pig4cloud.pig.common.log.event.SysLogEventSource;
import com.pig4cloud.pig.common.log.util.LogTypeEnum;
import com.pig4cloud.pig.common.log.util.SysLogUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

/**
 * 系统日志切面类，通过SpringAOP实现操作日志的异步记录，记录每个接口的耗时时间
 */
@Aspect
@Slf4j
public class SysLogAspect {

    /**
     * 环绕通知方法，用于处理系统日志记录
     */
    @Around("@annotation(sysLog)")  //针对使用SysLog注解
    @SneakyThrows  //异常不抛出
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {
        String className = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", className, strMethodName);

        String value = sysLog.value();
        String expression = sysLog.expression();// expression当前表达式存在,会覆盖value的值
        log.info("expression:{}", expression);
        if (CharSequenceUtil.isNotBlank(expression)) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
            try {
                value = SysLogUtils.getValue(context, expression, String.class);
                log.info("value:{}", value);
            } catch (Exception e) {   //表达式异常,获取value的值
                log.error("@SysLog 解析表达式 {} 异常", expression);
            }
        }

        SysLogEventSource vo = SysLogUtils.getSysLog();
        vo.setTitle(value);
        // 获取请求body参数
        if (CharSequenceUtil.isBlank(vo.getParams())) {
            vo.setBody(point.getArgs());
        }
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj;
        try {
            obj = point.proceed();
        } catch (Exception e) {
            vo.setLogType(LogTypeEnum.ERROR.getType());
            vo.setException(e.getMessage());
            throw e;
        } finally {
            Long endTime = System.currentTimeMillis();
            vo.setTime(endTime - startTime);//发布时间event事件
            SpringContextHolder.publishEvent(new SysLogEvent(vo));
        }

        return obj;
    }

}
