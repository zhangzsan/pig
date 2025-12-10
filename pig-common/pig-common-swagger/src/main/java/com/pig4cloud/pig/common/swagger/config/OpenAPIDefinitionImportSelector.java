package com.pig4cloud.pig.common.swagger.config;

import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * OpenAPI 配置类，
 * 用于动态注册 OpenAPI 相关 Bean 定义
 * 1. 使用@Bean注解：在配置类中通过方法返回对象，并加上@Bean注解，但这种方式是静态的，不能在运行时根据条件动态改变Bean的定义和注册条件。
 * 2. 使用ImportBeanDefinitionRegistrar接口：这个接口允许我们在运行时根据条件动态注册Bean。它通常与@Import注解一起使用，通过编程方式向容器中添加Bean定义。
 * 3. 使用BeanDefinitionRegistryPostProcessor接口：这个接口也可以动态注册Bean，但它是在容器解析完所有Bean定义之后、实例化Bean之前执行的。它更侧重于对容器中已有的Bean定义进行修改或添加，而且执行时机相对较晚。
 */
public class OpenAPIDefinitionImportSelector implements ImportBeanDefinitionRegistrar {

    /**
     * 注册Bean定义，根据注解元数据配置OpenAPI相关Bean
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        // 获取注解元数据属性
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnablePigDoc.class.getName(), true);
        if (annotationAttributes == null) {
            return;
        }
        Object value = annotationAttributes.get("value");
        if (Objects.isNull(value)) {
            return;
        }
        // 创建OpenAPIDefinition Bean定义
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(OpenAPIDefinition.class);
        definition.addPropertyValue("path", value);
        definition.setPrimary(true);

        registry.registerBeanDefinition("openAPIDefinition", definition.getBeanDefinition());

        // 如果是微服务架构则，引入了服务发现声明相关的元数据配置
        Object isMicro = annotationAttributes.getOrDefault("isMicro", true);
        if (isMicro.equals(false)) {
            return;
        }
        //创建OpenAPIMetadataConfiguration Bean定义
        BeanDefinitionBuilder openAPIMetadata = BeanDefinitionBuilder.genericBeanDefinition(OpenAPIMetadataConfiguration.class);
        openAPIMetadata.addPropertyValue("path", value);
        registry.registerBeanDefinition("openAPIMetadata", openAPIMetadata.getBeanDefinition());
    }

}
