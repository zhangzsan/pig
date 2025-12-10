package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.common.core.util.Result;

/**
 * 字典项服务接
 */
public interface SysDictItemService extends IService<SysDictItem> {

	/**
	 * 删除字典项
	 */
	Result<Boolean> removeDictItem(Long id);

	/**
	 * 更新字典项
	 */
	Result<Boolean> updateDictItem(SysDictItem item);

}
