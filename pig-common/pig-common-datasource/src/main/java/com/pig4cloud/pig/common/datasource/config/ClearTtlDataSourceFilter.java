package com.pig4cloud.pig.common.datasource.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.springframework.core.Ordered;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * 清空上文的DS 设置避免污染当前线程
 */
public class ClearTtlDataSourceFilter extends GenericFilterBean implements Ordered {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		DynamicDataSourceContextHolder.clear(); //线程可能被线程池重用,携带了上一个请求的数据源上下文, 防止跨请求的数据源泄露,确保每个请求都从"干净"的状态开始
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            DynamicDataSourceContextHolder.clear(); // 后置清理(保证异常时也执行)
            //清除本次请求过程中设置的数据源信息,防止对后续请求造成污染,保线程归还到线程池时是"干净"的
        }
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
