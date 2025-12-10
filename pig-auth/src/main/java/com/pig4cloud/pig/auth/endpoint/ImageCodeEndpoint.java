package com.pig4cloud.pig.auth.endpoint;

import cn.hutool.core.lang.Validator;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 生成数字验证码的接口,生成图片验证
 */
@Slf4j
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor//针对final修饰的字段生成构造函数
public class ImageCodeEndpoint {

	private static final Integer DEFAULT_IMAGE_WIDTH = 100;

	private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

	/**
	 * 创建图形验证码并输出到响应流,randomStr用户缓存,登录时需要验证
	 */
	@SneakyThrows
	@GetMapping("/image")
	public void image(String randomStr, HttpServletResponse response) {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        //使用hutool工具类正则表达式验证手机号
		if (Validator.isMobile(randomStr)) {
			return;
		}

        //创建一个ArithmeticCaptcha对象,用于生成验证码图片
		String result = captcha.text();
        //将生成的验证码保存到redis缓存中，登录时进行校验,缓存60秒
		RedisUtils.set(CacheConstants.DEFAULT_CODE_KEY + randomStr, result, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
        // 输出到响应流中
		captcha.out(response.getOutputStream());
	}

}
