package com.pig4cloud.pig.common.datasource.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据源配置属性
 */
@Setter
@Getter
@ConfigurationProperties("spring.datasource")
public class DataSourceProperties {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * jdbc url
	 */
	private String url;

	/**
	 * 驱动类型
	 */
	private String driverClassName;

	/**
	 * 查询数据源的SQL
	 */
	private String queryDsSql = "select * from gen_datasource_conf where del_flag = '0'";

}
