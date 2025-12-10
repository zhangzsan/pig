
package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.admin.mapper.SysLogMapper;
import com.pig4cloud.pig.admin.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统日志服务实现类
 */
@SuppressWarnings("unused")
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	/**
	 * 分页查询系统日志
	 * @param page 分页参数
	 * @param sysLog 日志查询条件
	 */
	@Override
	public Page<SysLog> getLogPage(Page page, SysLogDTO sysLog) {
		return baseMapper.selectPage(page, buildQuery(sysLog));
	}

	/**
	 * 保存日志
	 * @param sysLog 日志对象
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveLog(SysLog sysLog) {
		baseMapper.insert(sysLog);
		return Boolean.TRUE;
	}

	/**
     * 查询日志列表
     *
     * @param sysLog 查询条件DTO对象
     * @return 日志列表
     */
	@Override
	public List<SysLog> listLogs(SysLogDTO sysLog) {
		return baseMapper.selectList(buildQuery(sysLog));
	}

	/**
	 * 构建查询条件
	 * @param sysLog 前端查询条件DTO
	 * @return 构建好的LambdaQueryWrapper对象
	 */
	private LambdaQueryWrapper<SysLog> buildQuery(SysLogDTO sysLog) {
		LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
		if (CharSequenceUtil.isNotBlank(sysLog.getLogType())) {
			wrapper.eq(SysLog::getLogType, sysLog.getLogType());
		}

		if (ArrayUtil.isNotEmpty(sysLog.getCreateTime())) {
			wrapper.ge(SysLog::getCreateTime, sysLog.getCreateTime()[0])
				.le(SysLog::getCreateTime, sysLog.getCreateTime()[1]);
		}

		return wrapper;
	}

}
