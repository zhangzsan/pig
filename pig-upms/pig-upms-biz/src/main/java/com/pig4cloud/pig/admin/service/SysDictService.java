/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.common.core.util.Result;

/**
 * 字典表服务接口 提供字典数据的增删改查及缓存同步功能
 */
public interface SysDictService extends IService<SysDict> {

	/**
	 * 根据ID列表删除字典
	 */
	Result<Void> removeDictByIds(Long[] ids);

	/**
	 * 更新字典
	 */
	Result<Boolean> updateDict(SysDict sysDict);

	/**
	 * 同步字典缓存(清空缓存)
	 */
	Result<Boolean> syncDictCache();

}
