package com.pig4cloud.pig.common.seata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pig4cloud.pig.common.core.factory.YamlPropertySourceFactory;

/**
 * 加载指定配置文件中的内容
 */
@PropertySource(value = "classpath:seata-config.yml", factory = YamlPropertySourceFactory.class)
@Configuration(proxyBeanMethods = false)
public class SeataAutoConfiguration {

}
