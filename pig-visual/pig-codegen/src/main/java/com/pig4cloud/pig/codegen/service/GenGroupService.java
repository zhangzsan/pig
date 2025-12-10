package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import com.pig4cloud.pig.codegen.util.vo.TemplateGroupDTO;

/**
 * 模板分组服务接口
 */
public interface GenGroupService extends IService<GenGroupEntity> {

	/**
	 * 保存生成模板组
	 */
	void saveGenGroup(TemplateGroupDTO genTemplateGroup);

	/**
	 * 删除分组极其关系
	 */
	void delGroupAndTemplate(Long[] ids);

	/**
	 * 查询group数据
	 */
	GroupVO getGroupVoById(Long id);

	/**
	 * 更新group数据
	 */
	void updateGroupAndTemplateById(GroupVO GroupVo);

}
