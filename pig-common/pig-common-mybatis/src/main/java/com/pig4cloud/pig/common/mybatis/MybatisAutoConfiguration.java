package com.pig4cloud.pig.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.pig4cloud.pig.common.mybatis.config.MybatisPlusMetaObjectHandler;
import com.pig4cloud.pig.common.mybatis.plugins.PigPaginationInnerInterceptor;
import com.pig4cloud.pig.common.mybatis.resolver.SqlFilterArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * MyBatis Plus统一自动配置类, 提供SQL过滤器、分页插件及审计字段自动填充等配置
 */
@Configuration(proxyBeanMethods = false)
public class MybatisAutoConfiguration implements WebMvcConfigurer {

	/**
	 * 添加SQL过滤器参数解析器,避免SQL注入
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SqlFilterArgumentResolver());
	}

	/**
	 * 创建并配置MybatisPlus分页拦截器
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PigPaginationInnerInterceptor());
		return interceptor;
	}

	/**
	 * 创建并返回MybatisPlusMetaObjectHandler实例,用于审计字段自动填充
	 */
	@Bean
	public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
		return new MybatisPlusMetaObjectHandler();
	}

}
