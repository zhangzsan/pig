package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenFieldType;

import java.util.Set;

/**
 * 列属性服务接口
 */
public interface GenFieldTypeService extends IService<GenFieldType> {

	/**
	 * 根据tableId,获取包列表
	 */
	Set<String> getPackageByTableId(String dsName, String tableName);

}
