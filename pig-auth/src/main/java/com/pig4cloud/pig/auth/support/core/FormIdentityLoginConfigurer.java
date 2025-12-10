package com.pig4cloud.pig.auth.support.core;

import com.pig4cloud.pig.auth.support.handler.FormAuthenticationFailureHandler;
import com.pig4cloud.pig.auth.support.handler.SsoLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * 基于授权码模式的统一认证登录配置类,适用于Spring Security和SAS
 */
public final class FormIdentityLoginConfigurer extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> {
            formLogin.loginPage("/token/login");          // 自定义登录页面
            formLogin.loginProcessingUrl("/oauth2/form"); // 表单提交地址
            formLogin.failureHandler(new FormAuthenticationFailureHandler()); // 失败处理器
        });

        http.logout(logout ->
                        logout.logoutUrl("/oauth2/logout")
                                .logoutSuccessHandler(new SsoLogoutSuccessHandler())
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)) // SSO登出成功处理

                .csrf(AbstractHttpConfigurer::disable);
    }

}
