package com.pig4cloud.pig.auth.config;

import com.pig4cloud.pig.auth.support.CustomerOAuth2AccessTokenGenerator;
import com.pig4cloud.pig.auth.support.core.CustomeOAuth2TokenCustomizer;
import com.pig4cloud.pig.auth.support.core.FormIdentityLoginConfigurer;
import com.pig4cloud.pig.auth.support.core.PigDaoAuthenticationProvider;
import com.pig4cloud.pig.auth.support.filter.PasswordDecoderFilter;
import com.pig4cloud.pig.auth.support.filter.ValidateCodeFilter;
import com.pig4cloud.pig.auth.support.handler.PigAuthenticationFailureEventHandler;
import com.pig4cloud.pig.auth.support.handler.PigAuthenticationSuccessEventHandler;
import com.pig4cloud.pig.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.pig4cloud.pig.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.pig4cloud.pig.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import com.pig4cloud.pig.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * 认证服务器配置类spring启动时创建
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

    private final OAuth2AuthorizationService authorizationService;

    private final PasswordDecoderFilter passwordDecoderFilter;

    private final ValidateCodeFilter validateCodeFilter;


    @PostConstruct
    public void init() {
        log.info("************************ AuthorizationServerConfiguration @PostConstruct 执行 ************************");
        log.info("************************ OAuth2AuthorizationService: {} ************************", authorizationService);
    }
    /**
     * Authorization Server配置仅对/oauth2/** 的请求有效
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) //优先级最高
    public SecurityFilterChain authorizationServer(HttpSecurity http) throws Exception {
        log.info("************************ AuthorizationServerConfiguration authorizationServer 执行 ************************");
        // 配置授权服务器的安全策略，只有/oauth2/**的请求才会走如下的配置
        http.securityMatcher("/oauth2/**");

        // 增加验证码过滤器,在校验usernamePasswordFilter前先验证validateCodeFilter
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        // 增加密码解密过滤器
        http.addFilterBefore(passwordDecoderFilter, UsernamePasswordAuthenticationFilter.class);

        // 应用 OAuth2AuthorizationServerConfigurer 的配置
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.with(authorizationServerConfigurer
                        .tokenEndpoint(tokenEndpoint->tokenEndpoint // 个性化认证授权端点
                             .accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                             .accessTokenResponseHandler(new PigAuthenticationSuccessEventHandler()) // 登录成功处理器
                             .errorResponseHandler(new PigAuthenticationFailureEventHandler())) // 登录失败处理器
                        .clientAuthentication(configurer->configurer // 个性化客户端认证
                             .errorResponseHandler(new PigAuthenticationFailureEventHandler()))// 处理客户端认证异常
                        .authorizationEndpoint(authorizationEndpoint->authorizationEndpoint // 个性化授权页面
                             .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)), Customizer.withDefaults());

        //所有的请求均需要认证
        http.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        // 设置Token存储的策略
        http.with(authorizationServerConfigurer
                        .authorizationService(authorizationService)// redis存储token的实现
                        .authorizationServerSettings(AuthorizationServerSettings.builder().issuer(SecurityConstants.PROJECT_LICENSE).build()),
                Customizer.withDefaults());

        // 设置授权码模式登录页面
        http.with(new FormIdentityLoginConfigurer(), Customizer.withDefaults());
        DefaultSecurityFilterChain securityFilterChain = http.build();

        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);

        return securityFilterChain;
    }

    /**
     * 令牌生成规则实现
     * client:username:uuid
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator() {
        log.info("************************ OAuth2TokenGenerator");
        CustomerOAuth2AccessTokenGenerator accessTokenGenerator = new CustomerOAuth2AccessTokenGenerator();
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * request -> xToken 注入请求转换器
     * 短路请求,匹配校验当converter部位null即退出
     * 从Request中提取token的值,依次校验处理
     */
    @Bean
    public AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2ResourceOwnerPasswordAuthenticationConverter(), new OAuth2ResourceOwnerSmsAuthenticationConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(), new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeAuthenticationConverter(), new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }

    /**
     * 注入授权模式实现提供方
     * 1. 密码模式  2. 短信登录
     */
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService oAuth2AuthorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                authenticationManager, oAuth2AuthorizationService, oAuth2TokenGenerator());

        OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2ResourceOwnerSmsAuthenticationProvider(
                authenticationManager, oAuth2AuthorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new PigDaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }

}
