package com.pig4cloud.pig.common.xss.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * XSS 防护配置属性类
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(PigXssProperties.PREFIX)
public class PigXssProperties {

	public static final String PREFIX = "security.xss";

	/**
	 * 开启xss
	 */
	private boolean enabled = true;

	/**
	 * 全局:对文件进行首尾trim
	 */
	private boolean trimText = true;

	/**
	 * 模式:clear清理(默认),escape转义
	 */
	private Mode mode = Mode.clear;

	/**
	 * [clear专用] prettyPrint，默认关闭：保留换行
	 */
	private boolean prettyPrint = false;

	/**
	 * [clear 专用] 使用转义,默认关闭
	 */
	private boolean enableEscape = false;

	/**
	 * 拦截的路由,默认为空
	 */
	private List<String> pathPatterns = new ArrayList<>();

	/**
	 * 放行的路由,默认为空
	 */
	private List<String> pathExcludePatterns = new ArrayList<>();

	public enum Mode {

		/**
		 * 清理
		 */
		clear,
		/**
		 * 转义
		 */
		escape,
		/**
		 * 校验，抛出异常
		 */
		validate;

	}

}
