package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysFile;
import com.pig4cloud.pig.admin.service.SysFileService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件管理控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Tag(description = "sys-file", name = "文件管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysFileController {

	private final SysFileService sysFileService;

	/**
	 * 分页查询文件信息
	 * @param page 分页参数对象
	 * @param sysFile 文件查询条件对象
	 */
	@Operation(summary = "分页查询", description = "分页查询")  //Swagger注解
	@GetMapping("/page")
	public Result<IPage<SysFile>> getFilePage(@ParameterObject Page<SysFile> page, @ParameterObject SysFile sysFile) {
		LambdaQueryWrapper<SysFile> wrapper = Wrappers.<SysFile>lambdaQuery()
			.like(StrUtil.isNotBlank(sysFile.getOriginal()), SysFile::getOriginal, sysFile.getOriginal());
		return Result.ok(sysFileService.page(page, wrapper));
	}

	/**
	 * 通过id删除文件管理
	 * @param ids 要删除的文件id数组
	 */
	@Operation(summary = "通过id删除文件管理", description = "通过id删除文件管理")
	@SysLog("删除文件管理")
	@DeleteMapping
	@HasPermission("sys_file_del")
	public Result<Boolean> removeById(@RequestBody Long[] ids) {
		for (Long id : ids) {
			sysFileService.removeFile(id);
		}
		return Result.ok();
	}

	/**
	 * 上传文件
	 * @param file 上传的文件资源
	 * @return 包含文件路径的R对象，格式为(/admin/bucketName/filename)
	 */
	@PostMapping(value = "/upload")
	public Result<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
		return sysFileService.uploadFile(file);
	}

	/**
	 * 获取文件并写入响应流
	 * @param bucket 桶名称
	 * @param fileName 文件路径/名称
	 * @param response HTTP响应对象
	 */
	@Inner(false)
	@GetMapping("/{bucket}/{fileName}")
	public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
		sysFileService.getFile(bucket, fileName, response);
	}

	/**
	 * 获取本地resources目录下的文件并写入响应流
	 * @param fileName 文件名称
	 * @param response HTTP响应对象，用于输出文件内容
	 */
	@SneakyThrows
	@GetMapping("/local/file/{fileName}")
	public void localFile(@PathVariable String fileName, HttpServletResponse response) {
		ClassPathResource resource = new ClassPathResource("file/" + fileName);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IoUtil.copy(resource.getInputStream(), response.getOutputStream());
	}

}
