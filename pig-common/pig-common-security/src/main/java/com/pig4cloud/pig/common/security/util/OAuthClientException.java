package com.pig4cloud.pig.common.security.util;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * OAuth客户端异常类
 */
public class OAuthClientException extends OAuth2AuthenticationException {

	/**
	 * 使用指定消息构造OAuthClientException
	 */
	public OAuthClientException(String msg) {
		super(new OAuth2Error(msg), msg);
	}

	/**
	 * 构造一个带有指定消息和根原因的OAuthClientException
	 */
	public OAuthClientException(String msg, Throwable cause) {
		super(new OAuth2Error(msg), cause);
	}

}
