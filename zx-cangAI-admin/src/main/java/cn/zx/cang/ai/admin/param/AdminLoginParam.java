package cn.zx.cang.ai.admin.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author kinchou
 */
@Setter
@Getter
public class AdminLoginParam extends AdminRegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
