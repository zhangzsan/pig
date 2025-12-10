package com.pig4cloud.pig.admin.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.vo.DeptExcelVo;
import com.pig4cloud.pig.admin.mapper.SysDeptMapper;
import com.pig4cloud.pig.admin.service.SysDeptService;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import lombok.AllArgsConstructor;

/**
 * 部门管理服务实现类
 *
 */
@SuppressWarnings("unused")
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

	private final SysDeptMapper deptMapper;

	/**
	 * 根据部门ID删除部门（包含级联删除子部门）
	 * @param id 要删除的部门ID
	 * @return 删除操作是否成功，始终返回true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeDeptById(Long id) {
		// 级联删除部门
		List<Long> idList = this.listDescendants(id).stream().map(SysDept::getDeptId).toList();

		Optional.of(idList).filter(CollUtil::isNotEmpty).ifPresent(this::removeByIds);

		return Boolean.TRUE;
	}

	/**
	 * 查询部门树结构
	 * @param deptName 部门名称(模糊查询)
	 * @return 部门树结构列表，模糊查询时返回平铺列表
	 */
	@Override
	public List<Tree<Long>> getDeptTree(String deptName) {
		// 查询全部部门
		List<SysDept> deptAllList = deptMapper
			.selectList(Wrappers.<SysDept>lambdaQuery().like(CharSequenceUtil.isNotBlank(deptName), SysDept::getName, deptName));
        /*
         *
         */
		List<TreeNode<Long>> collect = deptAllList.stream()
//			.filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
			.sorted(Comparator.comparingInt(SysDept::getSortOrder))
			.map(dept -> {
				TreeNode<Long> treeNode = new TreeNode<>();
				treeNode.setId(dept.getDeptId());
				treeNode.setParentId(dept.getParentId());
				treeNode.setName(dept.getName());
				treeNode.setWeight(dept.getSortOrder());
				// 有权限不返回标识
				Map<String, Object> extra = new HashMap<>(8);
				extra.put(SysDept.Fields.createTime, dept.getCreateTime());
				treeNode.setExtra(extra);
				return treeNode;
			})
			.toList();

		// 模糊查询 不组装树结构 直接返回 表格方便编辑
		if (CharSequenceUtil.isNotBlank(deptName)) {
			return collect.stream().map(node -> {
				Tree<Long> tree = new Tree<>();
				tree.putAll(node.getExtra());
				BeanUtils.copyProperties(node, tree);
				return tree;
			}).toList();
		}

		return TreeUtil.build(collect, 0L);
	}

	/**
	 * 导出部门列表为Excel视图对象列表
	 * @return 部门Excel视图对象列表，包含部门名称、父部门名称和排序号
	 */
	@Override
	public List<DeptExcelVo> exportDepths() {
		List<SysDept> list = this.list();
        return list.stream().map(item -> {
            DeptExcelVo deptExcelVo = new DeptExcelVo();
            deptExcelVo.setName(item.getName());
            Optional<String> first = this.list()
                .stream()
                .filter(it -> item.getParentId().equals(it.getDeptId()))
                .map(SysDept::getName)
                .findFirst();
            deptExcelVo.setParentName(first.orElse("根部门"));
            deptExcelVo.setSortOrder(item.getSortOrder());
            return deptExcelVo;
        }).toList();
	}

	/**
	 * 导入部门信息,此导入不会讲已经导入的数据回滚
	 * @param excelVOList 部门Excel数据列表
	 * @param bindingResult 数据校验结果
	 */
	@Override
	public Result<Object> importDept(List<DeptExcelVo> excelVOList, BindingResult bindingResult) {
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		List<SysDept> deptList = this.list();
		for (DeptExcelVo item : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			boolean exitUsername = deptList.stream().anyMatch(sysDept -> item.getName().equals(sysDept.getName()));
			if (exitUsername) {
				errorMsg.add("部门名称已经存在");
			}
			SysDept one = this.getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getName, item.getParentName()));
			if (item.getParentName().equals("根部门")) {
				one = new SysDept();
				one.setDeptId(0L);
			}
			if (one == null) {
				errorMsg.add("上级部门不存在");
			}
            assert one != null;
            if (CollUtil.isEmpty(errorMsg)) {
				SysDept sysDept = new SysDept();
				sysDept.setName(item.getName());
                sysDept.setParentId(one.getDeptId());
				sysDept.setSortOrder(item.getSortOrder());
				baseMapper.insert(sysDept);
			} else {	// 数据不合法情况
                assert errorMessageList != null;
                errorMessageList.add(new ErrorMessage(item.getLineNum(), errorMsg));
			}
		}
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return Result.failed(errorMessageList);
		}
		return Result.ok(null, "部门导入成功");
	}

	/**
	 * 查询部门及其所有子部门
	 */
	@Override
	public List<SysDept> listDescendants(Long deptId) {
		// 查询全部部门
		List<SysDept> allDeptList = baseMapper.selectList(Wrappers.emptyWrapper());

		// 递归查询所有子节点
		List<SysDept> resDeptList = new ArrayList<>();
		recursiveDept(allDeptList, deptId, resDeptList);

		// 添加当前节点
		resDeptList.addAll(allDeptList.stream().filter(sysDept -> deptId.equals(sysDept.getDeptId())).toList());
		return resDeptList;
	}

	/**
	 * 递归查询所有子节点
	 */
	private void recursiveDept(List<SysDept> allDeptList, Long parentId, List<SysDept> resDeptList) {
		// 使用 Stream API 进行筛选和遍历
		allDeptList.stream().filter(sysDept -> sysDept.getParentId().equals(parentId)).forEach(sysDept -> {
			resDeptList.add(sysDept);
			recursiveDept(allDeptList, sysDept.getDeptId(), resDeptList);
		});
	}

}
