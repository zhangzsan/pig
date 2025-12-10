package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 用户岗位表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserPost extends Model<SysUserPost> {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 岗位ID
	 */
	@Schema(description = "岗位id")
	private Long postId;

}
