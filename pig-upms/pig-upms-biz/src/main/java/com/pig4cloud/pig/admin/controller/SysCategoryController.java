package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysCategory;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.pig.admin.service.SysCategoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 种类管理
 *
 * @author Allen
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/category" )
@Tag(description = "sys/category" , name = "种类管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysCategoryController {

    private final SysCategoryService sysCategoryService;

    /**
     * 分页查询
     * @param page 分页对象
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    public Result<Page<SysCategory>> getSysCategoryPage(@ParameterObject Page<SysCategory> page, @ParameterObject SysCategory sysCategory) {
        LambdaQueryWrapper<SysCategory> wrapper = Wrappers.lambdaQuery();
        return Result.ok(sysCategoryService.page(page, wrapper));
    }


    /**
     * 通过条件查询种类管理
     * @param sysCategory 查询条件
     */
    @Operation(summary = "通过条件查询" , description = "通过条件查询对象" )
    @GetMapping("/details" )
    @HasPermission("admin_sys_category_view")
    public Result<List<SysCategory>> getDetails(@ParameterObject SysCategory sysCategory) {
        return Result.ok(sysCategoryService.list(Wrappers.query(sysCategory)));
    }

    /**
     * 新增种类管理
     * @param sysCategory 种类管理
     */
    @Operation(summary = "新增种类管理" , description = "新增种类管理" )
    @SysLog("新增种类管理" )
    @PostMapping
    @HasPermission("admin_sys_category_add")
    public Result<Boolean> save(@RequestBody SysCategory sysCategory) {
        return Result.ok(sysCategoryService.save(sysCategory));
    }

    /**
     * 修改种类管理
     * @param sysCategory 种类管理
     */
    @Operation(summary = "修改种类管理" , description = "修改种类管理" )
    @SysLog("修改种类管理" )
    @PutMapping
    @HasPermission("admin_sys_category_edit")
    public Result<Boolean> updateById(@RequestBody SysCategory sysCategory) {
        return Result.ok(sysCategoryService.updateById(sysCategory));
    }

    /**
     * 通过id删除种类管理
     * @param ids categoryId列表
     */
    @Operation(summary = "通过id删除种类管理" , description = "通过id删除种类管理" )
    @SysLog("通过id删除种类管理" )
    @DeleteMapping
    @HasPermission("admin_sys_category_del")
    public Result<Boolean> removeById(@RequestBody Long[] ids) {
        return Result.ok(sysCategoryService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * sysCategory 查询条件
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("admin_sys_category_export")
    public List<SysCategory> exportExcel(SysCategory sysCategory, Long[] ids) {
        return sysCategoryService.list(Wrappers.lambdaQuery(sysCategory).in(ArrayUtil.isNotEmpty(ids), SysCategory::getCategoryId, (Object) ids));
    }

    /**
     * 导入excel 表
     * sysCategoryList 对象实体列表
     * bindingResult 错误信息列表
     */
    @PostMapping("/import")
    @HasPermission("admin_sys_category_export")
    public Result<Boolean> importExcel(@RequestExcel List<SysCategory> sysCategoryList, BindingResult bindingResult) {
        return Result.ok(sysCategoryService.saveBatch(sysCategoryList));
    }
}
