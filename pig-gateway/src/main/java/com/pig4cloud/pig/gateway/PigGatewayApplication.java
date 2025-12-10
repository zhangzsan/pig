package com.pig4cloud.pig.gateway;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关应用
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class PigGatewayApplication {

	public static void main(String[] args) {
//		SpringApplication.run(PigGatewayApplication.class, args);
        new SpringApplicationBuilder(PigGatewayApplication.class)
                .bannerMode(Banner.Mode.OFF) // 关键代码：关闭Banner
                .run(args);
	}

}
