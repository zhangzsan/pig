package com.pig4cloud.pig.common.xss.core;

import com.pig4cloud.pig.common.xss.utils.XssUtil;
import org.jsoup.Jsoup;

/**
 * xss 清理器
 *
 */
public interface XssCleaner {

	/**
	 * 清理 html
	 */
	default String clean(String html) {
		return clean(html, XssType.FORM);
	}

	/**
	 * 清理 html
	 */
	String clean(String html, XssType type);

	/**
	 * 判断输入是否安全
	 */
	default boolean isValid(String html) {
		return Jsoup.isValid(html, XssUtil.WHITE_LIST);
	}

}
