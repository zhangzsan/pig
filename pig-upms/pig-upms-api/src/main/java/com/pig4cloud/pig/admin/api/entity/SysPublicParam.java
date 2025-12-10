
package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 公共参数配置

 */
@Schema(description = "公共参数")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class SysPublicParam extends Model<SysPublicParam> {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "公共参数编号")
	private Long publicId;

	/**
	 * 公共参数名称
	 */
	@Schema(description = "公共参数名称", example = "公共参数名称")
	private String publicName;

	/**
	 * 公共参数地址值,英文大写+下划线
	 */
	@Schema(description = "键[英文大写+下划线]", example = "PIGX_PUBLIC_KEY")
	private String publicKey;

	/**
	 * 值
	 */
	@Schema(description = "值",  example = "999")
	private String publicValue;

	/**
	 * 状态（1有效；2无效；）
	 */
	@Schema(description = "标识[1有效；2无效]", example = "1")
	private String status;

	/**
	 * 公共参数编码
	 */
	@Schema(description = "编码", example = "^(PIG|PIGX)$")
	private String validateCode;

	/**
	 * 是否是系统内置
	 */
	@Schema(description = "是否是系统内置")
	private String systemFlag;

	/**
	 * 配置类型：0-默认；1-检索；2-原文；3-报表；4-安全；5-文档；6-消息；9-其他
	 */
	@Schema(description = "类型[1-检索；2-原文...]", example = "1")
	private String publicType;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改人")
	private String updateBy;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

}
