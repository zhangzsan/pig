package com.pig4cloud.pig.auth;

import com.pig4cloud.pig.common.feign.annotation.EnablePigFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 认证授权中心应用启动类
 */
@EnablePigFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PigAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigAuthApplication.class, args);
	}

}
