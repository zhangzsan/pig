
package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.mapper.SysOauthClientDetailsMapper;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OAuth2客户端详情服务实现类
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SysOauthClientDetailsServiceImpl extends ServiceImpl<SysOauthClientDetailsMapper, SysOauthClientDetails>
		implements SysOauthClientDetailsService {

	/**
	 * 根据客户端信息更新客户端详情
	 * @param clientDetails 客户端详情信息
	 */
	@Override    //删除缓存没有采用重试机制,可考虑使用消息队列保证缓存和数据库的最终一致性
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientDetails.clientId")
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateClientById(SysOauthClientDetails clientDetails) {
		this.insertOrUpdate(clientDetails);
		return Boolean.TRUE;
	}

	/**
	 * 保存客户端信息
	 * @param clientDetails 客户端详细信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveClient(SysOauthClientDetails clientDetails) {
		this.insertOrUpdate(clientDetails);
		return Boolean.TRUE;
	}

	/**
     * 插入或更新客户端对象
     * @param clientDetails 客户端详情对象
     */
	private void insertOrUpdate(SysOauthClientDetails clientDetails) {
		saveOrUpdate(clientDetails);
    }

	/**
	 * 分页查询OAuth客户端详情
	 * @param page 分页参数
	 * @param query 查询条件
	 * @return 分页查询结果
	 */
	@Override
	public Page<SysOauthClientDetails> getClientPage(Page<SysOauthClientDetails> page, SysOauthClientDetails query) {
		return baseMapper.selectPage(page, Wrappers.query(query));
	}

	/**
	 * 同步客户端缓存
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)  //Spring cache的注解CacheEvict指定要清除的缓存名称,设置为true表示清除该缓存名称下的所有条目
	public Result<Void> syncClientCache() {
		return Result.ok();
	}

}
