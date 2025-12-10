
package com.pig4cloud.pig.admin.service;

import com.pig4cloud.pig.common.core.util.Result;

/**
 * 系统手机服务接口：提供手机验证码发送功能
 */
public interface SysMobileService {

	/**
	 * 发送手机验证码
	 */
	Result<Boolean> sendSmsCode(String mobile);

}
