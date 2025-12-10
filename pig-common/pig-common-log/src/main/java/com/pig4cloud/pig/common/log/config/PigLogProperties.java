package com.pig4cloud.pig.common.log.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 日志配置类
 */
@Getter
@Setter
@ConfigurationProperties(PigLogProperties.PREFIX)
public class PigLogProperties {

    public static final String PREFIX = "security.log";

    /**
     * 开启日志记录
     */
    private boolean enabled = true;

    /**
     * 不写入日志中的内容 password,mobile,idcard,phone
     */
    private List<String> excludeFields = new ArrayList<>(Arrays.asList("password", "mobile", "idcard", "phone"));

    /**
     * 请求报文最大存储长度
     */
    private Integer maxLength = 2000;

}
