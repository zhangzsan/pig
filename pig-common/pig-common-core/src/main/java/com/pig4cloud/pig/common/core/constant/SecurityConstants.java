package com.pig4cloud.pig.common.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
/**
 * 安全相关常量
 */
public class SecurityConstants {

    /**
     * 角色前缀
     */
    public static final String ROLE = "ROLE_";

    /**
     * 前缀
     */
    public static final String PROJECT_PREFIX = "pig";

    /**
     * 项目的license
     */
    public static final String PROJECT_LICENSE = "https://pig4cloud.com";

    /**
     * 内部
     */
    public static final String FROM_IN = "Y";

    /**
     * 标志
     */
    public static final String FROM = "from";

    /**
     * 默认登录URL
     */
    public static final String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * grant_type
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * password 模式
     */
    public static final String PASSWORD = "password";

    /**
     * 授权码
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 手机号登录
     */
    public static final String MOBILE = "mobile";

    /**
     * {bcrypt} 加密的特征码
     */
    public static final String BCRYPT = "{bcrypt}";

    /**
     * {noop} 加密的特征码
     */
    public static final String NOOP = "{noop}";

    /**
     * 用户名
     */
    public static final String USERNAME = "username";

    /**
     * 用户信息
     */
    public static final String DETAILS_USER = "user_info";

    /**
     * 用户ID
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 协议字段
     */
    public static final String DETAILS_LICENSE = "license";

    /**
     * 验证码有效期,默认 60秒
     */
    public static final long CODE_TIME = 60;

    /**
     * 验证码长度
     */
    public static final String CODE_SIZE = "6";

    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 客户端ID
     */
    public static final String CLIENT_ID = "clientId";

    /**
     * 短信登录 参数名称
     */
    public static final String SMS_PARAMETER_NAME = "mobile";

    /**
     * 授权码模式confirm
     */
    public static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/confirm_access";

}
