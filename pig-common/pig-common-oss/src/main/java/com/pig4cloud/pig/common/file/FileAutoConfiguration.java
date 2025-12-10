package com.pig4cloud.pig.common.file;

import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.local.LocalFileAutoConfiguration;
import com.pig4cloud.pig.common.file.oss.OssAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * AWS 自动配置类
 */
@Import({ LocalFileAutoConfiguration.class, OssAutoConfiguration.class })
@EnableConfigurationProperties({ FileProperties.class })
public class FileAutoConfiguration {

}
