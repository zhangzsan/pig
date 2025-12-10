package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.admin.service.SysDictItemService;
import com.pig4cloud.pig.admin.service.SysDictService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典表前端控制器
 */
@RestController
@RequestMapping("/dict")
@Tag(description = "dict", name = "字典管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDictController {

	private final SysDictService sysDictService;

	private final SysDictItemService sysDictItemService;

    public SysDictController(SysDictService sysDictService, SysDictItemService sysDictItemService) {
        this.sysDictService = sysDictService;
        this.sysDictItemService = sysDictItemService;
    }

    /**
	 * 通过ID查询字典信.可能返回null
	 * @param id 字典ID
	 */
	@GetMapping("/details/{id}")
	public Result<SysDict> getById(@PathVariable Long id) {
		return Result.ok(sysDictService.getById(id));
	}

	/**
	 * 查询字典详细信息
	 * @param query 字典查询条件对象
	 */
	@GetMapping("/details")
	public Result<SysDict> getDetails(@ParameterObject SysDict query) {
		return Result.ok(sysDictService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 分页查询字典信息
	 * @param page 分页对象
	 * @param sysDict 字典查询条件
	 * @return 包含分页结果的响应对象
	 */
	@GetMapping("/page")
	public Result<IPage<SysDict>> getDictPage(@ParameterObject Page<SysDict> page, @ParameterObject SysDict sysDict) {
		return Result.ok(sysDictService.page(page,
				Wrappers.<SysDict>lambdaQuery()
					.eq(StrUtil.isNotBlank(sysDict.getSystemFlag()), SysDict::getSystemFlag, sysDict.getSystemFlag())
					.like(StrUtil.isNotBlank(sysDict.getDictType()), SysDict::getDictType, sysDict.getDictType())));
	}

	/**
	 * 保存字典信息
	 * @param sysDict 字典信息对象
	 */
	@SysLog("添加字典")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_dict_add')")
	public Result<Boolean> saveDict(@Valid @RequestBody SysDict sysDict) {
        boolean save = sysDictService.save(sysDict);
        return Result.ok(save);
	}

	/**
	 * 删除字典并清除字典缓存
	 * @param ids 字典ID数组
	 * @return 操作结果
	 */
	@SysLog("删除字典")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('sys_dict_del')")
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result<Void> removeById(@RequestBody Long[] ids) {
		return sysDictService.removeDictByIds(ids);
	}

	/**
	 * 修改字典信息
	 * @param sysDict 字典信息
	 */
	@PutMapping
	@SysLog("修改字典")
	@PreAuthorize("@pms.hasPermission('sys_dict_edit')")
	public Result<Boolean> updateDict(@Valid @RequestBody SysDict sysDict) {
		return sysDictService.updateDict(sysDict);
	}

	/**
	 * 分页查询字典列表
	 * @param name 字典类型名称或描述模糊查询
	 */
	@GetMapping("/list")
	public Result<List<SysDict>> listDicts(String name) {
		return Result.ok(sysDictService.list(Wrappers.<SysDict>lambdaQuery()
			.like(StrUtil.isNotBlank(name), SysDict::getDictType, name)
			.or()
			.like(StrUtil.isNotBlank(name), SysDict::getDescription, name)));
	}

	/**
	 * 分页查询字典项
	 * @param page 分页对象
	 * @param sysDictItem 字典项查询条件
	 */
	@GetMapping("/item/page")
	public Result<Page<SysDictItem>> getDictItemPage(Page<SysDictItem> page, SysDictItem sysDictItem) {
		return Result.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
	}

	/**
	 * 通过id查询字典项详情
	 * @param id 字典项id
	 */
	@GetMapping("/item/details/{id}")
	public Result<SysDictItem> getDictItemById(@PathVariable("id") Long id) {
		return Result.ok(sysDictItemService.getById(id));
	}

	/**
	 * 获取字典项详情
	 * @param query 字典项查询条件
	 * @return 包含字典项详情的响应结果
	 */
	@GetMapping("/item/details")
	public Result<SysDictItem> getDictItemDetails(SysDictItem query) {
		return Result.ok(sysDictItemService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 新增字典项
	 * @param sysDictItem 字典项对象
	 * @return 操作结果
	 */
	@SysLog("新增字典项")
	@PostMapping("/item")
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public Result<Boolean> saveDictItem(@RequestBody SysDictItem sysDictItem) {
		return Result.ok(sysDictItemService.save(sysDictItem));
	}

	/**
	 * 修改字典项
	 * @param sysDictItem 要修改的字典项对象
	 * @return 操作结果
	 */
	@SysLog("修改字典项")
	@PutMapping("/item")
	public Result<Boolean> updateDictItem(@RequestBody SysDictItem sysDictItem) {
		return sysDictItemService.updateDictItem(sysDictItem);
	}

	/**
	 * 通过id删除字典项
	 * @param id 字典项id
	 */
	@SysLog("删除字典项")
	@DeleteMapping("/item/{id}")
	public Result<Boolean> removeDictItemById(@PathVariable Long id) {
		return sysDictItemService.removeDictItem(id);
	}

	/**
	 * 同步字典缓存
	 * @return 操作结果
	 */
	@SysLog("同步字典")
	@PutMapping("/sync")
	public Result<Boolean> syncDict() {
		return sysDictService.syncDictCache();
	}

	/**
	 * 导出字典项数据
	 * @param sysDictItem 字典项查询条件
	 * @return 符合条件的字典项列表
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<SysDictItem> exportDictItems(SysDictItem sysDictItem) {
		return sysDictItemService.list(Wrappers.query(sysDictItem));
	}

	/**
	 * 通过字典类型查找字典
	 * @param type 类型
	 * @return 同类型字典
	 */
	@GetMapping("/type/{type}")
	@Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
	public Result<List<SysDictItem>> getDictByType(@PathVariable String type) {
		return Result.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getDictType, type)));
	}

	/**
	 * 通过字典类型查找字典 (针对feign调用) TODO: 兼容性方案，代码重复
	 * @param type 类型
	 * @return 同类型字典
	 */
	@Inner
	@GetMapping("/remote/type/{type}")
	@Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
	public Result<List<SysDictItem>> getRemoteDictByType(@PathVariable String type) {
		return Result.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getDictType, type)));
	}

}
