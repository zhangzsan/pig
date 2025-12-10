package com.pig4cloud.pig.common.mybatis.config;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.PigClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * MybatisPlus自动填充处理器,用于实体类字段的自动填充
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

	/**
	 * 插入时自动填充字段, metaObject元对象,用于操作实体类属性
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		log.debug("mybatis plus start insert fill ....");
		LocalDateTime now = LocalDateTime.now();
		fillValIfNullByName("createTime", now, metaObject);
		fillValIfNullByName("updateTime", now, metaObject);
		fillValIfNullByName("createBy", getUserName(), metaObject);
		fillValIfNullByName("updateBy", getUserName(), metaObject);
		fillValIfNullByName("delFlag", CommonConstants.STATUS_NORMAL, metaObject);
        log.debug("mybatis plus end insert fill ....");
    }

	/**
	 * 更新时自动填充字段
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		log.debug("mybatis plus start update fill ....");
		fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject);
		fillValIfNullByName("updateBy", getUserName(), metaObject);
        log.debug("mybatis plus end update fill ....");
    }

	/**
     * 填充值,先判断是否有手动设置,优先手动设置的值,例如：job必须手动设置
     * fieldName  属性名
     * fieldVal   属性值
     */
	private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject) {
		if (fieldVal == null) {
			return;
		}

        //当属性没有set方法时直接返回
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 2. 如果用户有手动设置的值,则不执行默认填充的数据库
		Object userSetValue = metaObject.getValue(fieldName);
		String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
		if (CharSequenceUtil.isNotBlank(setValueStr)) {
			return;
		}
		// 当field类型相同时设置
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (PigClassUtils.isAssignableValue(getterType, fieldVal)) {
			metaObject.setValue(fieldName, fieldVal);
		}
	}

	/**
	 * 获取当前登录的用户名
	 */
	private String getUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 匿名接口直接返回
		if (authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}
		if (Optional.ofNullable(authentication).isPresent()) {
			return authentication.getName();
		}
		return null;
	}
}
