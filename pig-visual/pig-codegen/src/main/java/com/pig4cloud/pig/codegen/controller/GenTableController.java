package com.pig4cloud.pig.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenTable;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pig.codegen.service.GenTableColumnService;
import com.pig4cloud.pig.codegen.service.GenTableService;
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
 * 代码表管理控制器
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/table")
@Tag(description = "table", name = "代码表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTableController {

	private final GenTableColumnService tableColumnService;

	/**
	 * 表服务
	 */
	private final GenTableService tableService;

	/**
	 * 分页查询
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public Result<IPage<GenTable>> getTablePage(Page<GenTable> page, GenTable table) {
		return Result.ok(tableService.queryTablePage(page, table));
	}

	/**
	 * 通过id查询表信息（代码生成设置 + 表 + 字段设置）
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public Result<GenTable> getTableById(@PathVariable("id") Long id) {
		return Result.ok(tableService.getById(id));
	}

	/**
	 * 查询数据源所有表
	 */
	@GetMapping("/list/{dsName}")
	public Result<List<String>> listTables(@PathVariable("dsName") String dsName) {
		return Result.ok(tableService.queryTableList(dsName));
	}

	/**
	 * 获取表信息
	 */
	@GetMapping("/{dsName}/{tableName}")
	public Result<GenTable> getTable(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
		return Result.ok(tableService.queryOrBuildTable(dsName, tableName));
	}

	/**
	 * 查询表DDL语句
	 */
	@GetMapping("/column/{dsName}/{tableName}")
	public Result<List<String>> getTableColumn(@PathVariable("dsName") String dsName, @PathVariable String tableName) throws Exception {
		return Result.ok(tableService.queryTableColumn(dsName, tableName));
	}

	/**
	 * 查询表DDL语句
	 */
	@GetMapping("/ddl/{dsName}/{tableName}")
	public Result<String> getTableDdl(@PathVariable("dsName") String dsName, @PathVariable String tableName) throws Exception {
		return Result.ok(tableService.queryTableDdl(dsName, tableName));
	}

	/**
	 * 同步表信息
	 * @param dsName 数据源
	 * @param tableName 表名称
	 */
	@GetMapping("/sync/{dsName}/{tableName}")
	public Result<GenTable> syncTable(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
		// 表配置删除
		tableService.remove(
				Wrappers.<GenTable>lambdaQuery().eq(GenTable::getDsName, dsName).eq(GenTable::getTableName, tableName));
		// 字段配置删除
		tableColumnService.remove(Wrappers.<GenTableColumnEntity>lambdaQuery()
			.eq(GenTableColumnEntity::getDsName, dsName)
			.eq(GenTableColumnEntity::getTableName, tableName));
		return Result.ok(tableService.queryOrBuildTable(dsName, tableName));
	}

	/**
	 * 修改列属性
	 */
	@Operation(summary = "修改列属性", description = "修改列属性")
	@SysLog("修改列属性")
	@PutMapping
	public Result<Boolean> updateTable(@RequestBody GenTable table) {
		return Result.ok(tableService.updateById(table));
	}

	/**
	 * 修改表字段数据
	 */
	@PutMapping("/field/{dsName}/{tableName}")
	public Result<String> updateTableField(@PathVariable("dsName") String dsName, @PathVariable String tableName,
                                           @RequestBody List<GenTableColumnEntity> tableFieldList) {
		tableColumnService.updateTableField(dsName, tableName, tableFieldList);
		return Result.ok();
	}

	/**
	 * 导出excel 表格
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<GenTable> exportTables(GenTable table) {
		return tableService.list(Wrappers.query(table));
	}

}
