package com.pig4cloud.pig.codegen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 动态查询Mapper接口
 *
 */
@Mapper
public interface GenDynamicMapper {

	/**
	 * 动态SQL查询
	 * @param sq SQL查询语句
	 */
	@InterceptorIgnore(tenantLine = "true")
	List<LinkedHashMap<String, Object>> dynamicQuerySql(@Param("value") String sq);

}
