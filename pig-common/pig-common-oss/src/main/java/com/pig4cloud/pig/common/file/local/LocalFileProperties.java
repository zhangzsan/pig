package com.pig4cloud.pig.common.file.local;

import lombok.Data;

/**
 * 本地文件 配置信息
 */
@Data
public class LocalFileProperties {

	/**
	 * 是否开启
	 */
	private boolean enable;

	/**
	 * 默认路径
	 */
	private String basePath;

}
