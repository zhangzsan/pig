package com.pig4cloud.pig.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 字段类型映射器接口：用于操作字段类型相关数据库操作
 */
@Mapper
public interface GenFieldTypeMapper extends BaseMapper<GenFieldType> {

	/**
	 * 根据tableId，获取包列表
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 */
	Set<String> getPackageByTableId(@Param("dsName") String dsName, @Param("tableName") String tableName);

}
