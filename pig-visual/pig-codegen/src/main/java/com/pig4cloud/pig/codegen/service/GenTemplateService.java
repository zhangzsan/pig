package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pig.common.core.util.Result;

/**
 * 代码生成模板服务接口
 */
public interface GenTemplateService extends IService<GenTemplateEntity> {

	/**
	 * 检查版本信息
	 */
	Result<Boolean> checkVersion();

	/**
	 * 在线更新
	 */
	Result<Object> onlineUpdate();

}
