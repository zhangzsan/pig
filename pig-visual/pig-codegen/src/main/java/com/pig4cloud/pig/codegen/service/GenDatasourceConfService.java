package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;

/**
 * 数据源配置服务接口 提供数据源的增删改查及校验等功能
 */
public interface GenDatasourceConfService extends IService<GenDatasourceConf> {

	/**
	 * 保存数据源并加密
	 */
	Boolean saveDsByEnc(GenDatasourceConf genDatasourceConf);

	/**
	 * 更新数据源
	 */
	Boolean updateDsByEnc(GenDatasourceConf genDatasourceConf);

	/**
	 * 添加动态数据源
	 */
	void addDynamicDataSource(GenDatasourceConf datasourceConf);

	/**
	 * 校验数据源配置是否有效
	 */
	Boolean checkDataSource(GenDatasourceConf datasourceConf);

	/**
	 * 通过数据源ID删除数据源
	 */
	Boolean removeByDsId(Long[] dsIds);

}
