package com.pig4cloud.pig.common.feign.core;

import feign.RequestInterceptor;
import org.springframework.http.HttpHeaders;

/**
 * Feign请求连接关闭拦截器, 用于设置HTTP连接为关闭状态
 **/
public class PigFeignRequestCloseInterceptor implements RequestInterceptor {

	/**
	 * 设置连接关闭
	 */
	@Override
	public void apply(feign.RequestTemplate template) {
        template.header(HttpHeaders.CONNECTION, "close");
	}

}
