package com.pig4cloud.pig.common.xss.core;

import cn.hutool.core.util.ArrayUtil;
import com.pig4cloud.pig.common.xss.config.PigXssProperties;
import com.pig4cloud.pig.common.xss.utils.XssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * Jackson XSS 处理类，用于清理JSON数据中的XSS风险内容
 */
@Slf4j
@RequiredArgsConstructor
public class JacksonXssClean extends XssCleanDeserializerBase {

	private final PigXssProperties properties;

	private final XssCleaner xssCleaner;

	/**
	 * 清理文本内容，根据XSS防护设置进行处理
	 * @param name 属性名称
	 * @param text 待清理的文本
	 * @return 清理后的文本
	 */
	@Override
	public String clean(String name, String text) throws IOException {
		if (XssHolder.isEnabled() && Objects.isNull(XssHolder.getXssCleanIgnore())) {
			String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
			log.debug("******* Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			return value;
		}
		else if (XssHolder.isEnabled() && Objects.nonNull(XssHolder.getXssCleanIgnore())) {
			XssCleanIgnore xssCleanIgnore = XssHolder.getXssCleanIgnore();
			if (ArrayUtil.contains(xssCleanIgnore.value(), name)) {
				return XssUtil.trim(text, properties.isTrimText());
			}

			String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
			log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			return value;
		}
		else {
			return XssUtil.trim(text, properties.isTrimText());
		}
	}

}
