package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程客户端详情服务接口
 *
 */
@FeignClient(contextId = "remoteClientDetailsService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteClientDetailsService {

	/**
	 * 通过clientId 查询客户端信息 (未登录，需要无token 内部调用)
	 */
	@NoToken
	@GetMapping("/client/getClientDetailsById/{clientId}")
    Result<SysOauthClientDetails> getClientDetailsById(@PathVariable("clientId") String clientId);

}
