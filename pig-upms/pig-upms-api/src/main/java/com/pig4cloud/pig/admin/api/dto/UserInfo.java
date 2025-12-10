
package com.pig4cloud.pig.admin.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息实体类，继承自UserVO并实现Serializable接口 , spring security
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "spring security 用户信息")
public class UserInfo extends UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 密码
     */
    @JsonIgnore(value = false)
    private String password;

    /**
     * 随机盐
     */
    @JsonIgnore(value = false)
    private String salt;

    /**
     * 权限标识集合
     */
    @Schema(description = "权限标识集合")
    private List<String> permissions = new ArrayList<>();

}
