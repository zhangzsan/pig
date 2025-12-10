package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.text.NamingCase;

/**
 * 命名规则处理工具类，提供驼峰、下划线等命名格式转换功能
 */
public class NamingCaseTool {

	/**
	 * 根据字段名生成对应的get方法名
	 */
	public static String getProperty(String in) {
		return String.format("get%s", NamingCase.toPascalCase(in));
	}

	/**
	 * 根据输入字符串生成setter方法名
	 */
	public static String setProperty(String in) {
		return String.format("set%s", NamingCase.toPascalCase(in));
	}

	/**
	 * 将字符串转换为帕斯卡命名格式（首字母大写）
	 */
	public static String pascalCase(String in) {
		return String.format(NamingCase.toPascalCase(in));
	}

}
