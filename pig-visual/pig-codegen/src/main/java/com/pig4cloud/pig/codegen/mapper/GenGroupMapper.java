
package com.pig4cloud.pig.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 模板分组 Mapper 接口
 */
@Mapper
public interface GenGroupMapper extends BaseMapper<GenGroupEntity> {

	/**
	 * 根据ID获取分组VO对象
	 * @param id 分组ID
	 * @return 分组VO对象
	 */
	GroupVO getGroupVoById(@Param("id") Long id);

}
