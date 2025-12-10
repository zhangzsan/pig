package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import com.pig4cloud.pig.codegen.mapper.GenFieldTypeMapper;
import com.pig4cloud.pig.codegen.service.GenFieldTypeService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 列属性
 */
@Service
public class GenFieldTypeServiceImpl extends ServiceImpl<GenFieldTypeMapper, GenFieldType>
		implements GenFieldTypeService {

    /**
     *  返回对应不数据源的表中的字段类型集合
     */
	@Override
	public Set<String> getPackageByTableId(String dsName, String tableName) {
		Set<String> importList = baseMapper.getPackageByTableId(dsName, tableName);
		return importList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet());
	}

}
