package com.pig4cloud.pig.common.datasource.support;

import lombok.NoArgsConstructor;
/**
 * 数据源相关常量
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DataSourceConstants {

	/**
	 * 数据源名称
	 */
	public static final String NAME = "name";

	/**
	 * 默认数据源(master)
	 */
    public static final String DS_MASTER = "master";

	/**
	 * jdbc url
	 */
    public static final  String DS_JDBC_URL = "url";

	/**
	 * 配置类型
	 */
    public static final  String DS_CONFIG_TYPE = "conf_type";

	/**
	 * 数据库连接用户名
	 */
    public static final  String DS_USER_NAME = "username";

	/**
	 * 数据库连接密码
	 */
    public static final  String DS_USER_PWD = "password";

	/**
	 * 数据库类型,如mysql, oracle等数据库
	 */
    public static final  String DS_TYPE = "ds_type";

	/**
	 * 数据库名称
	 */
    public static final  String DS_NAME = "ds_name";

	/**
	 * 主机类型
	 */
    public static final  String DS_HOST = "host";

	/**
	 * 端口
	 */
    public static final  String DS_PORT = "port";

	/**
	 * 实例名称
	 */
    public static final String DS_INSTANCE = "instance";

}
