package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysPublicParam;
import com.pig4cloud.pig.common.core.util.Result;

/**
 * 系统公共参数配置表 服务类
 */
public interface SysPublicParamService extends IService<SysPublicParam> {

	/**
	 * 根据公共参数key获取对应的value值
	 */
	String getParamValue(String publicKey);

	/**
	 * 更新系统公共参数
	 */
	Result<Boolean> updateParam(SysPublicParam sysPublicParam);

	/**
	 * 根据ID删除参数
	 */
	Result<Boolean> removeParamByIds(Long[] publicIds);

	/**
	 * 同步参数缓存
	 */
	Result<Void> syncParamCache();

}
