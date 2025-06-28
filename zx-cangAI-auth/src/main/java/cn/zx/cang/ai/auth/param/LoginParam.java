package cn.zx.cang.ai.auth.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Setter
@Getter
public class LoginParam extends RegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
