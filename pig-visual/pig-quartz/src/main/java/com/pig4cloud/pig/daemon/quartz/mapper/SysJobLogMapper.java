package com.pig4cloud.pig.daemon.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.daemon.quartz.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务执行日志表 Mapper 接口
 */
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {

}
