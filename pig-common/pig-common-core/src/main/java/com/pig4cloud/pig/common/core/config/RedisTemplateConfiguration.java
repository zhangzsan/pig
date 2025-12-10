package com.pig4cloud.pig.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 配置类
 */
@EnableCaching     //启用Spring的缓存支持
@AutoConfiguration
@AutoConfigureBefore(RedisAutoConfiguration.class)  //覆盖默认的RedisTemplate
public class RedisTemplateConfiguration {


	@Bean
	@Primary
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		// 创建一个新的RedisTemplate实例，用于String类型的键和Object类型的值
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// 设置键的序列化器为字符串序列化器，使键具有可读性
		redisTemplate.setKeySerializer(RedisSerializer.string());
		// 设置哈希键的序列化器为字符串序列化器，使哈希键具有可读性
		redisTemplate.setHashKeySerializer(RedisSerializer.string());
		// 设置值的序列化器为Java原生序列化器，用于序列化对象值
		redisTemplate.setValueSerializer(RedisSerializer.java());
		// 设置哈希值的序列化器为Java原生序列化器,用于序列化哈希对象值
		redisTemplate.setHashValueSerializer(RedisSerializer.java());
		// 设置Redis连接工厂，用于建立与Redis服务器的连接
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}

    /**
     *  处理hash
     */
	@Bean
	public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForHash();
	}

    /**
     * 处理字符串value
     */
	@Bean
	public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForValue();
	}

    /**
     *  处理list
     */
	@Bean
	public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

    /**
     *  处理set
     */
	@Bean
	public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForSet();
	}

    /**
     *  处理zSet
     */
	@Bean
	public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForZSet();
	}

}
