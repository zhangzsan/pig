package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import com.pig4cloud.pig.common.core.util.Result;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * 系统用户服务接口
 * 提供用户信息查询、分页查询、增删改查等操作
 */
@SuppressWarnings("unused")
public interface SysUserService extends IService<SysUser> {

	/**
	 * 根据用户信息查询用户详情
	 */
	Result<UserInfo> getUserInfo(UserDTO query);

	/**
	 * 分页查询用户信息(包含角色信息)
	 */
	IPage<UserDTO> getUsersWithRolePage(Page<UserDTO> page, UserDTO userDTO);

	/**
	 * 删除用户
	 */
	Boolean removeUserByIds(Long[] ids);

	/**
	 * 更新当前用户基本信息
	 */
	Result<Boolean> updateUserInfo(UserDTO userDto);

	/**
	 * 更新指定用户信息
	 */
	Boolean updateUser(UserDTO userDto);

	/**
	 * 通过ID查询用户信息
	 */
	UserVO getUserById(Long id);

	/**
	 * 保存用户信息
	 */
	Boolean saveUser(UserDTO userDto);

	/**
	 * 查询全部的用户
	 */
	List<UserExcelVO> listUsers(UserDTO userDTO);

	/**
	 * excel 导入用户
	 */
	Result<Object> importUsers(List<UserExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * 用户注册
     */
	Result<Boolean> registerUser(RegisterUserDTO userDto);

	/**
	 * 锁定用户
	 */
	Result<Boolean> lockUser(String username);

	/**
	 * 修改用户密码
	 */
    Result<Void> changePassword(UserDTO userDto);

	/**
	 * 校验密码
	 */
	Result<Void> checkPassword(String password);

}
