package com.pig4cloud.pig;

import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 单体版本启动器,运行此模块即可启动整个系统
 */
@EnablePigDoc(value = "admin", isMicro = false)
@EnablePigResourceServer
@SpringBootApplication
public class PigBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigBootApplication.class, args);
    }

}
