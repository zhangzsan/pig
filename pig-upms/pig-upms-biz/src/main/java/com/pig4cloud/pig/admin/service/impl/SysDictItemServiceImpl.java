package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.admin.mapper.SysDictItemMapper;
import com.pig4cloud.pig.admin.service.SysDictItemService;
import com.pig4cloud.pig.admin.service.SysDictService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.DictTypeEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * 字典项服务实现类
 */
@SuppressWarnings("unused")
@Service
@AllArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

	private final SysDictService dictService;

	/**
	 * 删除字典项
	 * @param id 字典项ID
	 * @return 操作结果
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result<Boolean> removeDictItem(Long id) {
		// 根据ID查询字典ID
		SysDictItem dictItem = this.getById(id);
		SysDict dict = dictService.getById(dictItem.getDictId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return Result.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_DELETE_SYSTEM));
		}
		return Result.ok(this.removeById(id));
	}

	/**
	 * 更新字典项
	 * @param item 需要更新的字典项
	 * @return 操作结果，包含成功或失败信息
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#item.dictType")
	public Result<Boolean> updateDictItem(SysDictItem item) {
		// 查询字典
		SysDict dict = dictService.getById(item.getDictId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return Result.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
		}
		return Result.ok(this.updateById(item));
	}

}
