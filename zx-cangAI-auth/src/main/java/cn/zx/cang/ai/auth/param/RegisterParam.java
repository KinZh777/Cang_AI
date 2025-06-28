package cn.zx.cang.ai.auth.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Setter
@Getter
public class RegisterParam {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 邀请码
     */
    private String inviteCode;
}
