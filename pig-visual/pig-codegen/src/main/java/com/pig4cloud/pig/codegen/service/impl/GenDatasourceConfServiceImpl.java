package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;
import com.pig4cloud.pig.codegen.mapper.GenDatasourceConfMapper;
import com.pig4cloud.pig.codegen.service.GenDatasourceConfService;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.datasource.util.DsConfTypeEnum;
import com.pig4cloud.pig.common.datasource.util.DsJdbcUrlEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  保存数据源
 * 数据源配置服务实现类
 * 提供数据源的增删改查及校验功能，支持数据源密码加密存储
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenDatasourceConfServiceImpl extends ServiceImpl<GenDatasourceConfMapper, GenDatasourceConf>
		implements GenDatasourceConfService {

	private final StringEncryptor stringEncryptor;

	private final DataSourceCreator hikariDataSourceCreator;

	/**
	 * 保存数据源配置并进行加密处理
	 */
	@Override
	public Boolean saveDsByEnc(GenDatasourceConf conf) {
		// 校验配置合法性
		if (!checkDataSource(conf)) {
			return Boolean.FALSE;
		}

		// 添加动态数据源
		addDynamicDataSource(conf);

		// 更新数据库配置
		conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
		this.baseMapper.insert(conf);
		return Boolean.TRUE;
	}

	/**
	 * 更新加密数据源,原则是先删除,然后是从新添加
	 */
	@Override
	public Boolean updateDsByEnc(GenDatasourceConf conf) {
		if (!checkDataSource(conf)) {
			return Boolean.FALSE;
		}
		// 先移除
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.removeDataSource(baseMapper.selectById(conf.getId()).getName());

		// 再添加
		addDynamicDataSource(conf);

		// 更新数据库配置
		if (StrUtil.isNotBlank(conf.getPassword())) {
			conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
		}
		this.baseMapper.updateById(conf);
		return Boolean.TRUE;
	}

	/**
	 * 通过数据源ID删除数据源
     * 移除数据库和SpringContextHolder中的数据库连接
	 */
	@Override
	public Boolean removeByDsId(Long[] dsIds) {
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		this.baseMapper.selectByIds(CollUtil.toList(dsIds))
			.forEach(ds -> dynamicRoutingDataSource.removeDataSource(ds.getName()));
		this.baseMapper.deleteByIds(CollUtil.toList(dsIds));
		return Boolean.TRUE;
	}

	/**
	 * 添加动态数据源
	 */
	@Override
	public void addDynamicDataSource(GenDatasourceConf conf) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(conf.getName());
		dataSourceProperty.setUrl(conf.getUrl());
		dataSourceProperty.setUsername(conf.getUsername());
		dataSourceProperty.setPassword(conf.getPassword());
		DataSource dataSource = hikariDataSourceCreator.createDataSource(dataSourceProperty);

		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPoolName(), dataSource);
	}

	/**
	 * 校验数据源配置是否有效
	 * 数据源配置是否有效,true表示有效,false表示无效
	 */
	@Override
	public Boolean checkDataSource(GenDatasourceConf conf) {
		String url;
		// JDBC 配置形式
		if (DsConfTypeEnum.JDBC.getType().equals(conf.getConfType())) {
			url = conf.getUrl();
		} else if (DsJdbcUrlEnum.MSSQL.getDbName().equals(conf.getDsType())) {
			// 主机形式 sql server 特殊处理
			DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(conf.getDsType());
			url = String.format(urlEnum.getUrl(), conf.getHost(), conf.getPort(), conf.getDsName());
		} else {
			DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(conf.getDsType());
			url = String.format(urlEnum.getUrl(), conf.getHost(), conf.getPort(), conf.getDsName());
		}

		conf.setUrl(url);

		try (Connection ignored = DriverManager.getConnection(url, conf.getUsername(), conf.getPassword())) {
           return Boolean.TRUE;
		} catch (SQLException e) {
			log.error("数据源配置 {} , 获取链接失败", conf.getName(), e);
			throw new RuntimeException("数据库配置错误，链接失败");
		}
	}
}
