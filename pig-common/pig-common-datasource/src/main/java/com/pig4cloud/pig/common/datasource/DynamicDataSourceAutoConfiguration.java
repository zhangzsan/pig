package com.pig4cloud.pig.common.datasource;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsJakartaHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsJakartaSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.pig4cloud.pig.common.datasource.config.*;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源切换配置
 */
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

	/**
	 * 获取动态数据源提供者
	 * defaultDataSourceCreator 默认数据源创建器
	 * stringEncryptor 字符串加密器
     * 从数据库或配置中读取动态数据源信息,支持加密的数据源密码解密,创建和管理多个数据源实例
	 */
	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
			StringEncryptor stringEncryptor, DataSourceProperties properties) {
		return new JdbcDynamicDataSourceProvider(defaultDataSourceCreator, stringEncryptor, properties);
	}

	/**
	 * 主数据源提供程序
	 * 专门处理主数据源的创建,基于Spring Bootd 标准配置创建默认数据源
	 */
	@Bean
	public DynamicDataSourceProvider masterDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
			DataSourceProperties properties) {
		return new MasterDataSourceProvider(defaultDataSourceCreator, properties);
	}

	/**
	 * 获取默认数据源创建器
	 * 统一的数据源创建入口,支持多种连接池(HikariCP、Druid等),通过策略模式扩展不同的数据源创建器
	 */
	@Bean
	public DefaultDataSourceCreator defaultDataSourceCreator(HikariDataSourceCreator hikariDataSourceCreator) {
		DefaultDataSourceCreator defaultDataSourceCreator = new DefaultDataSourceCreator();
		List<DataSourceCreator> creators = new ArrayList<>();
		creators.add(hikariDataSourceCreator);
		defaultDataSourceCreator.setCreators(creators);
        return defaultDataSourceCreator;
	}

	/**
	 * 获取数据源处理器, 获取数据源链
     * 在动态数据源处理器链(DsProcessor)中，通常采用责任链模式。每个处理器都会尝试解析数据源名称,如果当前处理器解析成功(即找到了数据源名称)，
     * 则不会继续传递到下一个处理器,直接返回解析结果。如果当前处理器解析失败(没有找到数据源名称),则会将请求传递给下一个处理器继续解析
	 */
	@Bean
	public DsProcessor dsProcessor(BeanFactory beanFactory) {
		DsProcessor lastParamDsProcessor = new LastParamDsProcessor();
		DsProcessor headerProcessor = new DsJakartaHeaderProcessor();
		DsProcessor sessionProcessor = new DsJakartaSessionProcessor();
		DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
		spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
		lastParamDsProcessor.setNextProcessor(headerProcessor);
		headerProcessor.setNextProcessor(sessionProcessor);
		sessionProcessor.setNextProcessor(spelExpressionProcessor);
		return lastParamDsProcessor;
	}

	/**
	 * 获取清除TTL数据源过滤器
	 */
	@Bean
	public ClearTtlDataSourceFilter clearTtlDsFilter() {
		return new ClearTtlDataSourceFilter();
	}

}
