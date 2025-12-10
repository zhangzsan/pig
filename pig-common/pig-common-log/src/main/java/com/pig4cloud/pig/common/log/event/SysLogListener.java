package com.pig4cloud.pig.common.log.event;

import java.util.Objects;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.admin.api.feign.RemoteLogService;
import com.pig4cloud.pig.common.core.config.PigJavaTimeModule;
import com.pig4cloud.pig.common.log.config.PigLogProperties;

import lombok.SneakyThrows;

/**
 * 系统日志监听器：异步处理系统日志事件
 */
@Data
@Slf4j
public class SysLogListener implements InitializingBean {

    private RemoteLogService remoteLogService;

    private PigLogProperties logProperties;

    // new一个避免日志脱敏策略影响全局ObjectMapper
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new PigJavaTimeModule());
    }

    public SysLogListener(RemoteLogService remoteLogService, PigLogProperties logProperties) {
        this.remoteLogService = remoteLogService;
        this.logProperties = logProperties;
    }


    /**
     * 异步保存系统日志
     * 系统日志事件
     */
    @SneakyThrows
    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        try {
            SysLogEventSource source = (SysLogEventSource) event.getSource();
            SysLog sysLog = new SysLog();
            BeanUtils.copyProperties(source, sysLog);

            if (Objects.nonNull(source.getBody())) {
                String params = objectMapper.writeValueAsString(source.getBody());
                //截取参数中的长度最长为200
                sysLog.setParams(CharSequenceUtil.subPre(params, logProperties.getMaxLength()));
            }
            remoteLogService.saveLog(sysLog);
        } catch (Exception e) { // 添加日志记录或降级处理
            log.error("保存系统日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理过滤的内容部分
     */
    @Override
    public void afterPropertiesSet() {
        objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
        //获取过滤的字段名称数组
        String[] ignorableFieldNames = logProperties.getExcludeFields().toArray(new String[0]);

        FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
        objectMapper.setFilterProvider(filters);
    }

    /**
     * 属性过滤混合类：用于通过名称过滤属性
     *
     */
    @JsonFilter("filter properties by name")
    static class PropertyFilterMixIn {

    }

}
