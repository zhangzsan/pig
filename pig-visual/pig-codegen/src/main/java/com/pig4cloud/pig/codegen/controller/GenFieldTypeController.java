package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import com.pig4cloud.pig.codegen.service.GenFieldTypeService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 列属性管理控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/fieldtype")
@Tag(description = "fieldtype", name = "列属性管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenFieldTypeController {

	private final GenFieldTypeService fieldTypeService;

	/**
	 * 分页查询字段类型
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public Result<Page<GenFieldType>> getFieldTypePage(Page<GenFieldType> page, GenFieldType fieldType) {
		return Result.ok(fieldTypeService.page(page,
				Wrappers.<GenFieldType>lambdaQuery().like(CharSequenceUtil.isNotBlank(fieldType.getColumnType()), GenFieldType::getColumnType,
							fieldType.getColumnType())));
	}

	/**
	 * 查询列表
	 */
	@Operation(summary = "查询列表", description = "查询列表")
	@GetMapping("/list")
	public Result<List<GenFieldType>> listFieldTypes(GenFieldType fieldType) {
		return Result.ok(fieldTypeService.list(Wrappers.query(fieldType)));
	}

	/**
	 * 通过id查询列属性
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/details/{id}")
	public Result<GenFieldType> getFieldTypeById(@PathVariable("id") Long id) {
		return Result.ok(fieldTypeService.getById(id));
	}

	/**
	 * 根据查询条件获取字段类型详情
	 */
	@GetMapping("/details")
	public Result<GenFieldType> getFieldTypeDetails(GenFieldType query) {
		return Result.ok(fieldTypeService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 新增列属性
	 */
	@Operation(summary = "新增列属性", description = "新增列属性")
	@SysLog("新增列属性")
	@PostMapping
	public Result<Boolean> saveFieldType(@RequestBody GenFieldType fieldType) {
		return Result.ok(fieldTypeService.save(fieldType));
	}

	/**
	 * 修改列属性
	 */
	@Operation(summary = "修改列属性", description = "修改列属性")
	@SysLog("修改列属性")
	@PutMapping
	public Result<Boolean> updateFieldType(@RequestBody GenFieldType fieldType) {
		return Result.ok(fieldTypeService.updateById(fieldType));
	}

	/**
	 * 通过id批量删除列属性
	 */
	@Operation(summary = "通过id删除列属性", description = "通过id删除列属性")
	@SysLog("通过id删除列属性")
	@DeleteMapping
	public Result<Boolean> removeFieldTypeByIds(@RequestBody Long[] ids) {
		return Result.ok(fieldTypeService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<GenFieldType> exportFieldTypes(GenFieldType fieldType) {
		return fieldTypeService.list(Wrappers.query(fieldType));
	}

}
