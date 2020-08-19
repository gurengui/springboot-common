package com.lc.springboot.user.dto.request;

import com.lc.springboot.common.mybatisplus.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 用户 更新请求对象
* @author: liangc
* @date: 2020-08-17 17:18
* @version 1.0
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="User 更新请求对象", description="用户 更新请求实体对象")
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(value = "ID", required = true, example = "0")
    @NotNull(message = "ID不能为空")
    private Long id;


    @ApiModelProperty(value = "用户账号", required = true)
    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "用户密码不能为空")
    private String userPassword;

    @ApiModelProperty(value = "用户类型", required = true)
    @NotBlank(message = "用户类型不能为空")
    private String userType;

    @ApiModelProperty(value = "用户名称", required = true)
    @NotBlank(message = "用户名称不能为空")
    private String userName;

    @ApiModelProperty(value = "电子邮件", required = true)
    @NotBlank(message = "电子邮件不能为空")
    private String email;

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @ApiModelProperty(value = "用户状态 | 1：正常 0：注销", required = true)
    @NotNull(message = "用户状态 | 1：正常 0：注销不能为空")
    private Integer status;


}
