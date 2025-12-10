
package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import com.pig4cloud.pig.admin.api.vo.PostExcelVO;
import com.pig4cloud.pig.admin.service.SysPostService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息表管理控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(description = "post", name = "岗位信息表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysPostController {

	private final SysPostService sysPostService;

	/**
	 * 获取岗位列表
	 * @return 包含岗位列表的响应结果
	 */
	@GetMapping("/list")
	public Result<List<SysPost>> listPosts() {
		return Result.ok(sysPostService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询岗位信息
	 * @param page 分页参数对象
	 * @param sysPost 岗位查询条件对象，模糊查询
	 */
	@Operation(description = "分页查询", summary = "分页查询")
	@GetMapping("/page")
	@HasPermission("sys_post_view")
	public Result<IPage<SysPost>> getPostPage(@ParameterObject Page<SysPost> page, @ParameterObject SysPost sysPost) {
		return Result.ok(sysPostService.page(page, Wrappers.<SysPost>lambdaQuery()
			.like(CharSequenceUtil.isNotBlank(sysPost.getPostName()), SysPost::getPostName, sysPost.getPostName())));
	}

	/**
	 * 通过id查询岗位信息
	 * @param postId 岗位id
	 */
	@Operation(description = "通过id查询", summary = "通过id查询")
	@GetMapping("/details/{postId}")
	@HasPermission("sys_post_view")
	public Result<SysPost> getById(@PathVariable("postId") Long postId) {
		return Result.ok(sysPostService.getById(postId));
	}

	/**
	 * 查询岗位详细信息
	 * @param query 查询条件,精确查询
	 */
	@Operation(description = "查询角色信息", summary = "查询角色信息")
	@GetMapping("/details")
	@HasPermission("sys_post_view")
	public Result<SysPost> getDetails(SysPost query) {
		return Result.ok(sysPostService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 新增岗位信息
	 * @param sysPost 岗位信息对象
	 */
	@Operation(description = "新增岗位信息表", summary = "新增岗位信息表")
	@SysLog("新增岗位信息表")
	@PostMapping
	@HasPermission("sys_post_add")
	public Result<Boolean> savePost(@RequestBody SysPost sysPost) {
		return Result.ok(sysPostService.save(sysPost));
	}

	/**
	 * 修改岗位信息
	 * @param sysPost 岗位信息对象
	 */
	@Operation(description = "修改岗位信息表", summary = "修改岗位信息表")
	@SysLog("修改岗位信息表")
	@PutMapping
	@HasPermission("sys_post_edit")
	public Result<Boolean> updatePost(@RequestBody SysPost sysPost) {
		return Result.ok(sysPostService.updateById(sysPost));
	}

	/**
	 * 通过id批量删除岗位信息
	 * @param ids 岗位id数组
	 */
	@Operation(description = "通过id删除岗位信息表", summary = "通过id删除岗位信息表")
	@SysLog("通过id删除岗位信息表")
	@DeleteMapping
	@HasPermission("sys_post_del")
	public Result<Boolean> removeById(@RequestBody Long[] ids) {
		return Result.ok(sysPostService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出岗位信息到Excel表格
	 * @return 岗位信息Excel文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_post_export")
	public List<PostExcelVO> exportPosts() {
		return sysPostService.listPosts();
	}

	/**
	 * 导入岗位信息
	 * @param excelVOList 岗位Excel数据列表
	 * @param bindingResult 数据校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import")
	@HasPermission("sys_post_export")
	public Result<Object> importRole(@RequestExcel List<PostExcelVO> excelVOList, BindingResult bindingResult) {
		return sysPostService.importPost(excelVOList, bindingResult);
	}

}
