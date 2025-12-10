package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import com.pig4cloud.pig.admin.api.vo.PostExcelVO;
import com.pig4cloud.pig.common.core.util.Result;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * 岗位信息表
 */
public interface SysPostService extends IService<SysPost> {

	/**
	 * 获取岗位列表用于导出Excel
	 */
	List<PostExcelVO> listPosts();

	/**
	 * 导入岗位信息
	 */
	Result<Object> importPost(List<PostExcelVO> excelVOList, BindingResult bindingResult);

}
