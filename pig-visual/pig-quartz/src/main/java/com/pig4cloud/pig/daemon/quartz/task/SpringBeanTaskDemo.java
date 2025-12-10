package com.pig4cloud.pig.daemon.quartz.task;

import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Spring Bean任务演示类
 *
 */
@Slf4j
@Component("demo")
public class SpringBeanTaskDemo {

    /**
     * 演示方法，用于测试Spring Bean
     */
    @SneakyThrows
    public String demoMethod(String para) {
        log.info("测试于:{}，输入参数{}", LocalDateTime.now(), para);
        return PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType();
    }

}
