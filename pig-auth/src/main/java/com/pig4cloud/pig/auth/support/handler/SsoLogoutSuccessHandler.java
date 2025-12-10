package com.pig4cloud.pig.auth.support.handler;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * SSO登出成功处理器,根据客户端传入的跳转地址进行重定向
 */
public class SsoLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final String REDIRECT_URL = "redirect_url";

	/**
     * 登出成功处理
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		if (response == null) {
			return;
		}

		// 获取请求参数中是否包含 回调地址
		String redirectUrl = request.getParameter(REDIRECT_URL);
		if (CharSequenceUtil.isNotBlank(redirectUrl)) {
			response.sendRedirect(redirectUrl);
		}
		else if (CharSequenceUtil.isNotBlank(request.getHeader(HttpHeaders.REFERER))) {
			// 默认跳转referer 地址
			String referer = request.getHeader(HttpHeaders.REFERER);
			response.sendRedirect(referer);
		}
	}

}
