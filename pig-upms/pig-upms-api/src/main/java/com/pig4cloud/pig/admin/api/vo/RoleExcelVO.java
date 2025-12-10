package com.pig4cloud.pig.admin.api.vo;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import com.pig4cloud.plugin.excel.annotation.ExcelLine;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色excel 对应的实体
 */
@ColumnWidth(30)
@Data
@EqualsAndHashCode
public class RoleExcelVO implements Serializable {


	/**
	 * 导入时候回显行号
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 主键ID
	 */
	@ExcelProperty("角色编号")
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	@ExcelProperty("角色名称")
	private String roleName;

	/**
	 * 角色标识
	 */
	@NotBlank(message = "角色标识不能为空")
	@ExcelProperty("角色标识")
	private String roleCode;

	/**
	 * 角色描述
	 */
	@NotBlank(message = "角色描述不能为空")
	@ExcelProperty("角色描述")
	private String roleDesc;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;
}
