package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysFile;
import com.pig4cloud.pig.common.core.util.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件管理服务接口
 * 提供文件上传、获取、删除等操作
 */
public interface SysFileService extends IService<SysFile> {

	/**
	 * 上传文件
	 */
	Result<Map<String, String>> uploadFile(MultipartFile file);

	/**
	 * 从指定存储桶中获取文件并写入HTTP响应流
	 */
	void getFile(String bucket, String fileName, HttpServletResponse response);

	/**
     * 根据ID删除文件
     */
	void removeFile(Long id);

}
