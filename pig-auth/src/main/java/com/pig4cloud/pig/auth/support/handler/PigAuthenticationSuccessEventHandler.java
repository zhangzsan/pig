package com.pig4cloud.pig.auth.support.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.log.event.SysLogEvent;
import com.pig4cloud.pig.common.log.util.SysLogUtils;
import com.pig4cloud.pig.common.security.component.PigCustomOAuth2AccessTokenResponseHttpMessageConverter;
import com.pig4cloud.pig.common.security.service.PigUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 处理认证成功事件的处理器
 */
@Slf4j
public class PigAuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new PigCustomOAuth2AccessTokenResponseHttpMessageConverter();

	/**
	 * 用户认证成功时调用
	 */
	@SneakyThrows
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
		//获取验证成功后的数据信息
        Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
		if (MapUtil.isNotEmpty(map)) {
			PigUser userInfo = (PigUser) map.get(SecurityConstants.DETAILS_USER);
			log.info("******************************用户：{} 登录成功", userInfo.getName());
			SecurityContextHolder.getContext().setAuthentication(accessTokenAuthentication);
			SysLog logVo = SysLogUtils.getSysLog();
			logVo.setTitle("用户登录成功");
			String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
			if (CharSequenceUtil.isNotBlank(startTimeStr)) {
				Long startTime = Long.parseLong(startTimeStr);
				Long endTime = System.currentTimeMillis();
				logVo.setTime(endTime - startTime);
			}
			logVo.setCreateBy(userInfo.getName());
			SpringContextHolder.publishEvent(new SysLogEvent(logVo));  //记录登录日志信息
		}

		// 输出token
		sendAccessTokenResponse(request, response, accessTokenAuthentication);
	}

	/**
	 * 发送访问令牌响应
	 */
	private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,OAuth2AccessTokenAuthenticationToken accessTokenAuthentication ) throws IOException {

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}
		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		// 无状态注意删除context上下文的信息
		SecurityContextHolder.clearContext();

		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

}
