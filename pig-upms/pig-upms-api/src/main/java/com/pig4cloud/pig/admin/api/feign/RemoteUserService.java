package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程用户服务接口：提供用户信息查询功能
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteUserService {

	/**
	 * (未登录状态调用,需要加 @NoToken)通过用户名查询用户、角色信息
	 */
	@NoToken
	@GetMapping("/user/info/query")
    Result<UserInfo> info(@SpringQueryMap UserDTO user); //@SpringQueryMap将对象参数转换为URL查询参数

}
