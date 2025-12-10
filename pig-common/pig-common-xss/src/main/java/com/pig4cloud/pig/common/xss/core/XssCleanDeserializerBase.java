package com.pig4cloud.pig.common.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.IOException;

/**
 * Jackson XSS 处理基类
 */
public abstract class XssCleanDeserializerBase extends JsonDeserializer<String> {

	/**
	 * 反序列化方法，用于处理JSON字符串的反序列化并进行XSS清洗
	 * @param p JSON解析器
	 * @param ctx 反序列化上下文
	 * @return 经过XSS清洗后的字符串，如果输入为null则返回null
	 */
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
		JsonToken jsonToken = p.getCurrentToken();
		if (JsonToken.VALUE_STRING != jsonToken) {
			throw MismatchedInputException.from(p, String.class, "mica-xss: can't deserialize value of type java.lang.String from " + jsonToken);
		}
		// 解析字符串
		String text = p.getValueAsString();
		if (text == null) {
			return null;
		}

		// xss 配置
		return this.clean(p.getParsingContext().getCurrentName(), text);
	}

	/**
	 * 清理文本中的XSS攻击内容
	 * @param name 文本名称标识
	 * @param text 待清理的文本内容
	 * @return 清理后的安全文本
	 */
	public abstract String clean(String name, String text) throws IOException;

}
