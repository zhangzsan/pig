package com.pig4cloud.pig.common.feign.sentinel.parser;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Sentinel 请求头解析判断实现类，用于从HTTP请求头中获取Allow字段值
 */
public class PigHeaderRequestOriginParser implements RequestOriginParser {

	/**
	 * 请求头获取allow
	 */
	private static final String ALLOW = "Allow";

	/**
	 * 解析HTTP请求中的来源信息
	 * @param request HTTP请求对象
	 * @return 解析出的来源信息
	 */
	@Override
	public String parseOrigin(HttpServletRequest request) {
		return request.getHeader(ALLOW);
	}

}
