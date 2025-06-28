package cn.zx.cang.ai.admin.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Setter
@Getter
public class AdminRegisterParam {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    /**
     * 验证码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
