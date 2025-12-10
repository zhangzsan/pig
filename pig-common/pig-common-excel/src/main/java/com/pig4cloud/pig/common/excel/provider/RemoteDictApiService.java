package com.pig4cloud.pig.common.excel.provider;

import com.pig4cloud.pig.common.core.util.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.Map;

/**
 * 远程字典API服务接口，基于RestClient GetExchange实现
 */
public interface RemoteDictApiService {

	/**
	 * 根据类型获取字典数据
	 */
	@GetExchange("/dict/remote/type/{type}")
    Result<List<Map<String, Object>>> getDictByType(@PathVariable String type);

}
