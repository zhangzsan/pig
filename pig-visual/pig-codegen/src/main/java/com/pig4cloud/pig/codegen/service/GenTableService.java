package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenTable;
import org.anyline.metadata.Table;

import java.util.List;

/**
 * 代码生成表服务接口
 */
public interface GenTableService extends IService<GenTable> {

	/**
	 * 查询对应数据源的表
	 */
	IPage<GenTable> queryTablePage(Page<GenTable> page, GenTable table);

	/**
	 * 查询表信息(列),然后插入到中间表中
	 */
	GenTable queryOrBuildTable(String dsName, String tableName);

	/**
	 * 查询表ddl 语句
	 */
	String queryTableDdl(String dsName, String tableName) throws Exception;

	/**
	 * 查询数据源里面的全部表
	 */
	List<String> queryTableList(String dsName);

	/**
	 * 查询表的全部字段,dsName数据源名称，tableName表名
	 */
	List<String> queryTableColumn(String dsName, String tableName);

}
