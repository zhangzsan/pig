package com.pig4cloud.pig.common.core.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson配置类，用于自定义Jackson的ObjectMapper配置
 */
@AutoConfiguration  //2.7 后引入的配置类
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

	/**
	 * 自定义Jackson2ObjectMapperBuilder配置
	 */
	@Bean
	@ConditionalOnMissingBean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
            //设置默认日期时间格式
			builder.locale(Locale.CHINA);
            //设置系统默认时区
			builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            //默认日期时间格式
			builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            //配置Long类型序列化为字符串
			builder.serializerByType(Long.class, ToStringSerializer.instance);
            //注册自定义时间模块
			builder.modules(new PigJavaTimeModule());
		};
	}

}
