package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.codegen.service.GeneratorService;
import com.pig4cloud.pig.common.core.util.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
public class GeneratorController {

	private final GeneratorService generatorService;

	/**
	 * ZIP下载生成代码
	 */
	@SneakyThrows
	@GetMapping("/download")
	public void download(String tableIds, HttpServletResponse response) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		// 生成代码
		for (String tableId : tableIds.split(StrPool.COMMA)) {
			generatorService.downloadCode(Long.parseLong(tableId), zip);
		}

		IoUtil.close(zip);

		// zip压缩包数据
		byte[] data = outputStream.toByteArray();

		response.reset();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.zip", tableIds));
		response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
		response.setContentType("application/octet-stream; charset=UTF-8");
		IoUtil.write(response.getOutputStream(), false, data);
	}

	/**
	 * 生成代码
	 */
	@ResponseBody
	@GetMapping("/code")
	public Result<String> code(String tableIds) throws Exception {
		for (String tableId : tableIds.split(StrUtil.COMMA)) {
			generatorService.generatorCode(Long.valueOf(tableId));
		}
		return Result.ok();
	}

	/**
	 * 预览代码,根据表的id生成对应指定的代码
	 */
	@SneakyThrows
	@GetMapping("/preview")
	public List<Map<String, String>> preview(Long tableId) {
		return generatorService.preview(tableId);
	}

}
