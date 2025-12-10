package com.pig4cloud.pig.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 代码生成模板Mapper接口
 */
@Mapper
public interface GenTemplateMapper extends BaseMapper<GenTemplateEntity> {

	/**
	 * 根据模板组ID查询模板列表
	 * @param groupId 模板组ID
	 * @return 模板实体列表
	 */
	List<GenTemplateEntity> listTemplateById(Long groupId);

}
