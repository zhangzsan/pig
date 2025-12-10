
package com.pig4cloud.pig.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

	/**
	 * 根据用户DTO获取用户VO
	 * @param userDTO 用户查询条件DTO
	 * @return 用户信息VO
	 */
	UserVO getUser(@Param("query") UserDTO userDTO);

	/**
	 * 分页查询用户信息（含角色）
	 * @param page 分页参数
	 * @param userDTO 用户查询条件
	 */
	IPage<UserDTO> getUsersPage(Page<UserDTO> page, @Param("query") UserDTO userDTO);

	/**
	 * 查询用户列表
	 * @param userDTO 查询条件
	 * @return 用户VO列表
	 */
	List<UserVO> listUsers(@Param("query") UserDTO userDTO);

}
