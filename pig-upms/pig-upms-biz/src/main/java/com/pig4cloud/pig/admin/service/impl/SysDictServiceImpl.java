
package com.pig4cloud.pig.admin.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.admin.mapper.SysDictItemMapper;
import com.pig4cloud.pig.admin.mapper.SysDictMapper;
import com.pig4cloud.pig.admin.service.SysDictService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.DictTypeEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.Result;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;

/**
 * 系统字典服务实现类
 */
@SuppressWarnings("unused")
@Service
@AllArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	private final SysDictItemMapper dictItemMapper;

	/**
	 * 根据ID删除字典,删除数据并删除缓存
	 * @param ids 字典ID数组
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result<Void> removeDictByIds(Long[] ids) {
		List<Long> dictIdList = baseMapper.selectByIds(CollUtil.toList(ids))
			.stream()
			.filter(sysDict -> !sysDict.getSystemFlag().equals(DictTypeEnum.SYSTEM.getType()))// 系统内置类型不删除
			.map(SysDict::getId)
			.toList();

		baseMapper.deleteByIds(dictIdList);

		dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery().in(SysDictItem::getDictId, dictIdList));
		return Result.ok();
	}

	/**
	 * 更新字典数据
	 * @param dict 字典对象
	 * @return 操作结果
	 * @see Result 返回结果封装类
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#dict.dictType")
	public Result<Boolean> updateDict(SysDict dict) {
		SysDict sysDict = this.getById(dict.getId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystemFlag())) {
			return Result.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
		}
        boolean update = this.updateById(dict);
        return Result.ok(update);
	}

	/**
	 * 同步字典缓存（清空缓存）
	 * @return 操作结果
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result<Boolean> syncDictCache() {
		return Result.ok(true);
	}

}
