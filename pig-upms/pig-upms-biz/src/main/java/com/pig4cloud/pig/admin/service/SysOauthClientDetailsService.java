package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.common.core.util.Result;

/**
 * OAuth2客户端详情服务接口

 */
public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

	/**
	 * 根据客户端信息更新客户端详情
	 */
	Boolean updateClientById(SysOauthClientDetails clientDetails);

	/**
	 * 保存客户端信息
	 */
	Boolean saveClient(SysOauthClientDetails clientDetails);

	/**
	 * 分页查询OAuth客户端详情
	 */
	Page<SysOauthClientDetails> getClientPage(Page<SysOauthClientDetails> page, SysOauthClientDetails query);

	/**
	 * 同步客户端缓存
	 */
	Result<Void> syncClientCache();

}
