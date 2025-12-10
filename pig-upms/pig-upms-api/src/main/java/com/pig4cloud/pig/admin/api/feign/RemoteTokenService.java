package com.pig4cloud.pig.admin.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 远程令牌服务接口
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteTokenService {

	/**
	 * 分页查询token 信息
	 * @param params 分页参数
	 */
	@NoToken
	@PostMapping("/token/page")
    Result<Page> getTokenPage(@RequestBody Map<String, Object> params);

	/**
     * 根据token删除token信息
     */
	@NoToken
	@DeleteMapping("/token/remove/{token}")
    void removeTokenById(@PathVariable("token") String token);

	/**
	 * 根据令牌查询用户信息
	 */
	@NoToken
	@GetMapping("/token/query-token")
    Result<Map<String, Object>> queryToken(@RequestParam("token") String token);

}
