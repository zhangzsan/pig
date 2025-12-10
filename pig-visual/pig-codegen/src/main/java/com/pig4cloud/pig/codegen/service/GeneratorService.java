package com.pig4cloud.pig.codegen.service;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成服务接口
 */
public interface GeneratorService {

	/**
	 * 生成代码zip写出
	 */
	void downloadCode(Long tableId, ZipOutputStream zip);

	/**
	 * 预览代码
	 */
	List<Map<String, String>> preview(Long tableId);

	/**
	 * 目标目录写入渲染结果
	 */
	void generatorCode(Long tableId);

}
