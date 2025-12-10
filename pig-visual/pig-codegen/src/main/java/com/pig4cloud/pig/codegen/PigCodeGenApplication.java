package com.pig4cloud.pig.codegen;

import com.pig4cloud.pig.common.datasource.annotation.EnableDynamicDataSource;
import com.pig4cloud.pig.common.feign.annotation.EnablePigFeignClients;
import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 代码生成模块应用启动类
 */
@EnableDynamicDataSource
@EnablePigFeignClients
@EnablePigDoc("gen")
@EnableDiscoveryClient
@EnablePigResourceServer
@SpringBootApplication
public class PigCodeGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigCodeGenApplication.class, args);
	}

}
