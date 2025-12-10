package com.pig4cloud.pig.common.file.core;

import com.pig4cloud.pig.common.file.local.LocalFileProperties;
import com.pig4cloud.pig.common.file.oss.OssProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 文件配置信息,支持本地和oss两种配置文件上传方式
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperties {

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "local";

	/**
	 * 本地文件配置信息
	 */
	@NestedConfigurationProperty
	private LocalFileProperties local;

	/**
	 * oss 文件配置信息
	 */
	@NestedConfigurationProperty
	private OssProperties oss;

}
