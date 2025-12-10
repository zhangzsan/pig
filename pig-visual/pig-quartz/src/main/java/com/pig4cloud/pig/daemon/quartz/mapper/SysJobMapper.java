
package com.pig4cloud.pig.daemon.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务调度表 Mapper 接口
 */
@Mapper
public interface SysJobMapper extends BaseMapper<SysJob> {

}
