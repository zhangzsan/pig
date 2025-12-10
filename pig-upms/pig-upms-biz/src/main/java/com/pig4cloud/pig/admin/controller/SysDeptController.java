
package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.vo.DeptExcelVo;
import com.pig4cloud.pig.admin.service.SysDeptService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门管理前端控制器
 */
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
@Tag(description = "dept", name = "部门管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysDeptController {

	private final SysDeptService sysDeptService;

	/**
	 * 通过ID查询部门信息
	 */
	@GetMapping("/{id}")
	public Result<SysDept> getById(@PathVariable Long id) {
		return Result.ok(sysDeptService.getById(id));
	}

	/**
	 * 查询全部部门列表
	 */
    @GetMapping("/list")
	public Result<List<SysDept>> listDepts() {
		return Result.ok(sysDeptService.list());
	}

	/**
	 * 获取树形菜单
	 */
    @GetMapping(value = "/tree")
	public Result<List<Tree<Long>>> getDeptTree(String deptName) {
		return Result.ok(sysDeptService.getDeptTree(deptName));
	}

	/**
	 * 保存部门信息
	 */
	@SysLog("添加部门")
	@PostMapping
	@HasPermission("sys_dept_add")
	public Result<Boolean> saveDept(@Valid @RequestBody SysDept sysDept) {
		return Result.ok(sysDeptService.save(sysDept));
	}

	/**
	 * 根据ID删除部门,级联删除部门
	 * @param id 部门ID
	 */
	@SysLog("删除部门")
	@DeleteMapping("/{id}")
	@HasPermission("sys_dept_del")
	public Result<Boolean> removeById(@PathVariable Long id) {
		return Result.ok(sysDeptService.removeDeptById(id));
	}

	/**
	 * 编辑部门信息
	 * @param sysDept 部门实体对象
	 */
	@SysLog("编辑部门")
	@PutMapping
	@HasPermission("sys_dept_edit")
	public Result<Boolean> updateDept(@Valid @RequestBody SysDept sysDept) {
		sysDept.setUpdateTime(LocalDateTime.now());
		return Result.ok(sysDeptService.updateById(sysDept));
	}

	/**
	 * 获取当前部门及其下面的子部门
	 * @param deptId 部门ID
	 * @return 包含子级部门列表的响应结果
	 */
	@GetMapping(value = "/getDescendantList/{deptId}")
	public Result<List<SysDept>> getDescendantList(@PathVariable Long deptId) {
		return Result.ok(sysDeptService.listDescendants(deptId));
	}

	/**
	 * 导出部门数据
	 * @return 部门数据列表
	 */
	@ResponseExcel
	@GetMapping("/export")
	public List<DeptExcelVo> exportDepts() {
		return sysDeptService.exportDepths();
	}

	/**
	 * 导入部门信息
	 * @param excelVOList 部门Excel数据列表
	 * @param bindingResult 数据校验结果
	 */
	@PostMapping("import")
	public Result<Object> importDept(@RequestExcel List<DeptExcelVo> excelVOList, BindingResult bindingResult) {
		return sysDeptService.importDept(excelVOList, bindingResult);
	}

}
