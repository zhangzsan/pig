package com.pig4cloud.pig.common.file.oss;

import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.core.FileTemplate;
import com.pig4cloud.pig.common.file.oss.http.OssEndpoint;
import com.pig4cloud.pig.common.file.oss.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * AWS 对象存储自动配置类
 */
@RequiredArgsConstructor
public class OssAutoConfiguration {

    private final FileProperties properties;
    /**
     * 创建OssTemplate Bean
     *
     * @return 文件模板实例
     * @ ConditionalOnMissingBean 当容器中不存在OssTemplate Bean时创建
     * @ ConditionalOnProperty 当配置项file.oss.enable为true时生效
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnProperty(name = "file.oss.enable", havingValue = "true")
    public FileTemplate ossTemplate() {
        return new OssTemplate(properties);
    }

    /**
     * 创建OssEndpoint Bean
     * @ ConditionalOnMissingBean 当容器中不存在该类型Bean时创建
     * @ ConditionalOnProperty 当配置项file.oss.info为true时生效
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "file.oss.info", havingValue = "true")
    public OssEndpoint ossEndpoint(OssTemplate template) {
        return new OssEndpoint(template);
    }

}
