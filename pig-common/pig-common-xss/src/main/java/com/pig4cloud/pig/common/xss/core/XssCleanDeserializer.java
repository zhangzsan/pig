package com.pig4cloud.pig.common.xss.core;

import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.xss.config.PigXssProperties;
import com.pig4cloud.pig.common.xss.utils.XssUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Jackson XSS 处理反序列化器
 */
@Slf4j
public class XssCleanDeserializer extends XssCleanDeserializerBase {

	/**
	 * 清理文本中的XSS内容
	 * name 名称
	 * text 待清理的文本
	 */
	@Override
	public String clean(String name, String text) throws IOException {
		// 读取 xss 配置
		PigXssProperties properties = SpringContextHolder.getBean(PigXssProperties.class);
		// 读取 XssCleaner bean
		XssCleaner xssCleaner = SpringContextHolder.getBean(XssCleaner.class);
        String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
        log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
        return value;
    }

}
