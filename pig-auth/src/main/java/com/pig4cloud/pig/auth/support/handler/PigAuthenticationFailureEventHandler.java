package com.pig4cloud.pig.auth.support.handler;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.log.event.SysLogEvent;
import com.pig4cloud.pig.common.log.util.LogTypeEnum;
import com.pig4cloud.pig.common.log.util.SysLogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 认证失败处理器:处理用户认证失败事件并记录日志
 */
@Slf4j
public class PigAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

	private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

	/**
	 * 当认证失败时调用
	 */
	@Override
	@SneakyThrows
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		String username = request.getParameter(OAuth2ParameterNames.USERNAME);

		log.info("**********************用户：{} 登录失败, 异常：{}", username, exception.getLocalizedMessage());
		SysLog logVo = SysLogUtils.getSysLog();
		logVo.setTitle("用户登录失败");
		logVo.setLogType(LogTypeEnum.ERROR.getType());
		logVo.setException(exception.getLocalizedMessage());
		String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
		if (CharSequenceUtil.isNotBlank(startTimeStr)) {
			Long startTime = Long.parseLong(startTimeStr);
			Long endTime = System.currentTimeMillis();
			logVo.setTime(endTime - startTime);
		}
		logVo.setCreateBy(username);
		SpringContextHolder.publishEvent(new SysLogEvent(logVo));
		sendErrorResponse(request, response, exception);
	}

	/**
	 * 发送错误响应
	 */
	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		String errorMessage;

		if (exception instanceof OAuth2AuthenticationException authorizationException) {
            errorMessage = CharSequenceUtil.isBlank(authorizationException.getError().getDescription())
					? authorizationException.getError().getErrorCode()
					: authorizationException.getError().getDescription();
		} else {
			errorMessage = exception.getLocalizedMessage();
		}

		this.errorHttpResponseConverter.write(Result.failed(errorMessage), MediaType.APPLICATION_JSON, httpResponse);
	}

}
