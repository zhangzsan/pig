package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.mapper.SysUserMapper;
import com.pig4cloud.pig.admin.service.SysMobileService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 手机登录相关业务实现类
 */
@SuppressWarnings("unused")
@Slf4j
@Service
@AllArgsConstructor
public class SysMobileServiceImpl implements SysMobileService {

	private final SysUserMapper userMapper;

	/**
	 * 发送手机验证码
	 */
	@Override
	public Result<Boolean> sendSmsCode(String mobile) {
		List<SysUser> userList = userMapper
			.selectList(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, mobile));

		if (CollUtil.isEmpty(userList)) {
			log.info("手机号未注册:{}", mobile);
			return Result.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_PHONE_UNREGISTERED, mobile));
		}

		String cacheKey = CacheConstants.DEFAULT_CODE_KEY + mobile;
		String codeObj = RedisUtils.get(cacheKey);

		if (codeObj != null) {
			log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
			return Result.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_OFTEN));
		}

		String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
		log.info("手机号生成验证码成功:{},{}", mobile, code);
		RedisUtils.set(cacheKey, code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);

		// 集成短信服务发送验证码
		SmsBlend smsBlend = SmsFactory.getSmsBlend();
		if (Objects.isNull(smsBlend)) {
			return Result.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_SMS_BLEND_UNREGISTERED));
		}

		SmsResponse smsResponse = smsBlend.sendMessage(mobile, new LinkedHashMap<>(Map.of("code", code)));
		log.debug("调用短信服务发送验证码结果:{}", smsResponse);
		return Result.ok(Boolean.TRUE);
	}

}
