package com.pig4cloud.pig.common.file.core;

import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.util.List;

/**
 * 文件操作模板
 */
public interface FileTemplate extends InitializingBean {

	/**
	 * 创建bucket
	 */
	void createBucket(String bucketName);

	/**
	 * 获取全部bucket
	 */
	List<?> getAllBuckets();

	/**
	 * bucketName bucket名称
	 */
	void removeBucket(String bucketName);

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 */
	void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception;

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 */
	void putObject(String bucketName, String objectName, InputStream stream) throws Exception;

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 */
	Object getObject(String bucketName, String objectName);

	void removeObject(String bucketName, String objectName) throws Exception;

	/**
	 */
	@Override
	default void afterPropertiesSet() throws Exception {
	}

	/**
	 * 根据文件前置查询文件
	 * @param bucketName bucket名称
	 * @param prefix 前缀
	 * @param recursive 是否递归查询
	 * @return 文件对象列表
	 */
	List<?> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);

}
