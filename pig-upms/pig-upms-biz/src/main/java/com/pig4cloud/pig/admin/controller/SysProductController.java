package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysProductEntity;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.pig.admin.service.SysProductService;

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
 * 商品
 *
 * @author pig
 * @date 2025-12-04 22:27:09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys_product" )
@Tag(description = "sys_product" , name = "商品管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysProductController {

    private final  SysProductService sysProductService;


    @Operation(summary = "获取所有用户",description = "获取所有用户")
    public Result<?> getAllProduct(){
        List<SysProductEntity> list = sysProductService.list();
        return Result.ok(list);
    }

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @HasPermission("admin_sys_product_view")
    public Result getSysProductPage(@ParameterObject Page page, @ParameterObject SysProductEntity sysProduct) {
        LambdaQueryWrapper<SysProductEntity> wrapper = Wrappers.lambdaQuery();
        return Result.ok(sysProductService.page(page, wrapper));
    }


    /**
     * 通过条件查询商品
     * @param sysProduct 查询条件
     * @return R  对象列表
     */
    @Operation(summary = "通过条件查询" , description = "通过条件查询对象" )
    @GetMapping("/details" )
    @HasPermission("admin_sys_product_view")
    public Result getDetails(@ParameterObject SysProductEntity sysProduct) {
        return Result.ok(sysProductService.list(Wrappers.query(sysProduct)));
    }

    /**
     * 新增商品
     * @param sysProduct 商品
     * @return R
     */
    @Operation(summary = "新增商品" , description = "新增商品" )
    @SysLog("新增商品" )
    @PostMapping
    @HasPermission("admin_sys_product_add")
    public Result save(@RequestBody SysProductEntity sysProduct) {
        return Result.ok(sysProductService.save(sysProduct));
    }

    /**
     * 修改商品
     * @param sysProduct 商品
     * @return R
     */
    @Operation(summary = "修改商品" , description = "修改商品" )
    @SysLog("修改商品" )
    @PutMapping
    @HasPermission("admin_sys_product_edit")
    public Result updateById(@RequestBody SysProductEntity sysProduct) {
        return Result.ok(sysProductService.updateById(sysProduct));
    }

    /**
     * 通过id删除商品
     * @param ids productId列表
     * @return R
     */
    @Operation(summary = "通过id删除商品" , description = "通过id删除商品" )
    @SysLog("通过id删除商品" )
    @DeleteMapping
    @HasPermission("admin_sys_product_del")
    public Result removeById(@RequestBody Long[] ids) {
        return Result.ok(sysProductService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysProduct 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("admin_sys_product_export")
    public List<SysProductEntity> exportExcel(SysProductEntity sysProduct,Long[] ids) {
        return sysProductService.list(Wrappers.lambdaQuery(sysProduct).in(ArrayUtil.isNotEmpty(ids), SysProductEntity::getProductId, ids));
    }

    /**
     * 导入excel 表
     * @param sysProductList 对象实体列表
     * @param bindingResult 错误信息列表
     * @return ok fail
     */
    @PostMapping("/import")
    @HasPermission("admin_sys_product_export")
    public Result importExcel(@RequestExcel List<SysProductEntity> sysProductList, BindingResult bindingResult) {
        return Result.ok(sysProductService.saveBatch(sysProductList));
    }
}
