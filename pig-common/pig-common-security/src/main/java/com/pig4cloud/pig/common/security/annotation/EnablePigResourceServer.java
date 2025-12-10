package com.pig4cloud.pig.common.security.annotation;

import com.pig4cloud.pig.common.security.component.PigResourceServerAutoConfiguration;
import com.pig4cloud.pig.common.security.component.PigResourceServerConfiguration;
import com.pig4cloud.pig.common.security.feign.PigFeignClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用Pig资源服务器注解
 * 通过导入相关配置类启用Pig资源服务器功能
 */
@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({ PigResourceServerAutoConfiguration.class, PigResourceServerConfiguration.class, PigFeignClientConfiguration.class })
public @interface EnablePigResourceServer {

}
