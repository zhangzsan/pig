package com.pig4cloud.pig.codegen.util.vo;

import lombok.Data;

@Data
public class GenTemplateFileVO {

	/**
	 * 模板名称
	 */
	private String templateName;

	/**
	 * 路径
	 */
	private String generatorPath;

	/**
	 * 模板 desc
	 */
	private String templateDesc;

	/**
	 * 模板文件
	 */
	private String templateFile;

}
