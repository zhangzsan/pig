package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;

import java.util.List;

/**
 * 代码生成表列服务接口
 */
public interface GenTableColumnService extends IService<GenTableColumnEntity> {

	/**
	 * 初始化字段列表
	 */
	void initFieldList(List<GenTableColumnEntity> tableFieldList);

	/**
	 * 更新表字段信息
	 */
	void updateTableField(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList);

}
