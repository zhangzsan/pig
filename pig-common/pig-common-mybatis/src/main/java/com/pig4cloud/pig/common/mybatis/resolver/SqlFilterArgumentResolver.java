package com.pig4cloud.pig.common.mybatis.resolver;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Order By SQL注入问题解决类
 */
@Slf4j
public class SqlFilterArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * 判断Controller方法参数是否为Page类型
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Page.class);
	}

	/**
	 * 解析分页参数并构建Page对象
	 * @param parameter 方法参数信息
	 * @param mavContainer 模型和视图容器
	 * @param webRequest web请求对象
	 * @param binderFactory 数据绑定工厂
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        // 打印的分页MethodParameter参数
        log.debug("分页MethodParameter参数：{}", parameter);
        assert request != null;
        log.debug("分页参数：{}", request.getQueryString());
        //获取升序排序参数列表
		String[] asks = request.getParameterValues("ascs");
        //获取倒序参数列表
		String[] desks = request.getParameterValues("descs");
		String current = request.getParameter("current");
		String size = request.getParameter("size");

		Page<?> page = new Page<>();
		if (CharSequenceUtil.isNotBlank(current)) {
			page.setCurrent(Convert.toLong(current, 0L));
		}

		if (CharSequenceUtil.isNotBlank(size)) {
			page.setSize(Convert.toLong(size, 10L));
		}

		List<OrderItem> orderItemList = new ArrayList<>();
		Optional.ofNullable(asks)
			.ifPresent(s -> orderItemList
				.addAll(Arrays.stream(s).filter(asc -> !SqlInjectionUtils.check(asc)).map(OrderItem::asc).toList()));
		Optional.ofNullable(desks)
			.ifPresent(s -> orderItemList
				.addAll(Arrays.stream(s).filter(desc -> !SqlInjectionUtils.check(desc)).map(OrderItem::desc).toList()));
		page.addOrder(orderItemList);

		return page;
	}

}
