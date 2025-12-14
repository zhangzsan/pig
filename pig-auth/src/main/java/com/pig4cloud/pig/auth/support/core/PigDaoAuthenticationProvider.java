package com.pig4cloud.pig.auth.support.core;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.core.util.PigWebUtils;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.pig4cloud.pig.common.core.constant.SecurityConstants.PASSWORD;

/**
 * 基于DAO的认证提供者实现,用于处理用户名密码认证
 */
@Slf4j
public class PigDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/**
	 * 用户未找到时用于PasswordEncoder#matches(CharSequence, String)的明文密码，避免SEC-2056问题
	 */
	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

	private  static final BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

	/**
	 * 密码编码器
	 */
	private PasswordEncoder passwordEncoder;

	/**
	 * 用户未找到时的加密密码,用于避免SEC-2056问题,某些密码编码器在密码格式无效时会短路处理
	 */
	private volatile String userNotFoundEncodedPassword;

    /**
     * -- SETTER --
     *  设置用户详情服务
     */
    @Setter
    private UserDetailsService userDetailsService;

	private UserDetailsPasswordService userDetailsPasswordService;

	public PigDaoAuthenticationProvider() {
		setMessageSource(SpringUtil.getBean("securityMessageSource"));
		setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}

	/**
	 * 执行额外的身份验证检查
	 * userDetails 用户详细信息
	 * authentication 身份验证令牌
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		// 只有密码模式需要校验密码
		String grantType = PigWebUtils.getRequest().get().getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!CharSequenceUtil.equals(PASSWORD, grantType)) {
			return;
		}

		if (authentication.getCredentials() == null) {
			log.info("**************** Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			log.info("*********************** Failed to authenticate since password does not match stored value");
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	/**
	 * 根据用户名检索用户详情
	 * username 用户名
	 * authentication 认证令牌
     * 核心认证逻辑
	 */
	@SneakyThrows
	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
		prepareTimingAttackProtection();
		HttpServletRequest request = PigWebUtils.getRequest().orElseThrow((Supplier<Throwable>) () -> new InternalAuthenticationServiceException("web request is empty"));

		String grantType = PigWebUtils.getRequest().get().getParameter(OAuth2ParameterNames.GRANT_TYPE);
		String clientId = PigWebUtils.getRequest().get().getParameter(OAuth2ParameterNames.CLIENT_ID);

		if (CharSequenceUtil.isBlank(clientId)) {
			clientId = basicConvert.convert(request).getName();
		}

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(PigUserDetailsService.class);

		String finalClientId = clientId;
		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values()
			.stream()
			.filter(service -> service.support(finalClientId, grantType))
			.max(Comparator.comparingInt(Ordered::getOrder));

		if (optional.isEmpty()) {
			throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
		}

		try {
			UserDetails loadedUser = optional.get().loadUserByUsername(username);
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"UserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		} catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		} catch (InternalAuthenticationServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	/**
	 * 创建认证成功后的Authentication对象
	 * @param principal 认证主体
	 * @param authentication 认证信息
	 */
	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
		boolean upgradeEncoding = this.userDetailsPasswordService != null && this.passwordEncoder.upgradeEncoding(user.getPassword());
		if (upgradeEncoding) {
			String presentedPassword = authentication.getCredentials().toString();
			String newPassword = this.passwordEncoder.encode(presentedPassword);
			user = this.userDetailsPasswordService.updatePassword(user, newPassword);
		}
		return super.createSuccessAuthentication(principal, authentication, user);
	}

	/**
	 * 准备定时攻击保护,如果未找到用户编码密码为空则进行编码
	 */
	private void prepareTimingAttackProtection() {
		if (this.userNotFoundEncodedPassword == null) {
			this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		}
	}

	/**
	 * 防止时序攻击的缓解措施
	 * authentication 用户名密码认证令牌
	 */
	private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() != null) {
			String presentedPassword = authentication.getCredentials().toString();
			this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
		}
	}

	/**
	 * 设置用于编码和验证密码的PasswordEncoder实例
	 * passwordEncoder 密码编码器实例，不能为null
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
		this.userNotFoundEncodedPassword = null;
	}

	protected PasswordEncoder getPasswordEncoder() {
		return this.passwordEncoder;
	}

    protected UserDetailsService getUserDetailsService() {
		return this.userDetailsService;
	}

}
