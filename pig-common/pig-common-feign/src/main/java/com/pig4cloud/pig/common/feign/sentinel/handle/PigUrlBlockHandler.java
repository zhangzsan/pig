package com.pig4cloud.pig.common.feign.sentinel.handle;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.core.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Sentinel统一降级限流策略处理器, 实现BlockExceptionHandler接口，处理Sentinel限流降级异常
 */
@Slf4j
@RequiredArgsConstructor
public class PigUrlBlockHandler implements BlockExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String resourceName, BlockException e)
            throws Exception {
        log.error("sentinel 降级 资源名称{}", resourceName, e);

        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().print(objectMapper.writeValueAsString(Result.failed(e.getMessage())));
    }

}
