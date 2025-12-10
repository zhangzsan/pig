package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.admin.api.entity.SysLog;

import java.util.List;

/**
 * 日志表 服务类
 *
 */
public interface SysLogService extends IService<SysLog> {

	/**
	 * 分页查询系统日志
	 */
	Page<?> getLogPage(Page<?> page, SysLogDTO sysLog);

	/**
	 * 保存日志
	 */
	Boolean saveLog(SysLog sysLog);

	/**
	 * 查询日志列表
	 */
	List<SysLog> listLogs(SysLogDTO sysLog);

}
