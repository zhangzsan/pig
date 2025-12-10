package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程日志服务接口
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteLogService {

	/**
     *
     */
	@NoToken
	@PostMapping("/log/save")
    void saveLog(@RequestBody SysLog sysLog);

}
