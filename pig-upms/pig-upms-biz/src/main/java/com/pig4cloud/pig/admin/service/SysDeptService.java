
package com.pig4cloud.pig.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.vo.DeptExcelVo;
import com.pig4cloud.pig.common.core.util.Result;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * 部门管理服务接口
 */
public interface SysDeptService extends IService<SysDept> {

	/**
	 * 查询部门树菜单
	 */
	List<Tree<Long>> getDeptTree(String deptName);

	/**
	 * 根据部门ID删除部门
	 */
	Boolean removeDeptById(Long id);

	/**
	 * 导出部门Excel数据列表
	 */
	List<DeptExcelVo> exportDepths();

	/**
	 * 导入部门数据
	 */
	Result<Object> importDept(List<DeptExcelVo> excelVOList, BindingResult bindingResult);

	/**
	 * 获取指定部门的所有后代部门列表
	 */
	List<SysDept> listDescendants(Long deptId);

}
