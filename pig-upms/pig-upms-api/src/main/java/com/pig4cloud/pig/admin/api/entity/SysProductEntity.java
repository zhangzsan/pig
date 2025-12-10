package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品
 *
 * @author pig
 * @date 2025-12-04 22:27:09
 */
@Data
@TableName("sys_product")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品")
public class SysProductEntity extends Model<SysProductEntity> {


	/**
	* 商品ID
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="商品ID")
    private Long productId;

	/**
	* 商品名称
	*/
    @Schema(description="商品名称")
    private String name;

	/**
	* 排序
	*/
    @Schema(description="排序")
    private Integer sortOrder;

	/**
	* 商品种类ID
	*/
    @Schema(description="商品种类ID")
    private Long categoryId;

	/**
	* 图片路径
	*/
    @Schema(description="图片路径")
    private String image;

	/**
	* 商品库存
	*/
    @Schema(description="商品库存")
    private Integer stock;

	/**
	* 商品的SKU
	*/
    @Schema(description="商品的SKU")
    private String SKU;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 修改人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改人")
    private String updateBy;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

	/**
	* 删除标志
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标志")
    private String delFlag;
}
